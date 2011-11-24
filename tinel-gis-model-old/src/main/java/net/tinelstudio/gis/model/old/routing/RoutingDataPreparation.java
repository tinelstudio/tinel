
package net.tinelstudio.gis.model.old.routing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * class contains methods for correcting data and for preparing routing graph
 * 
 * @author Ivan Fucak
 */
public class RoutingDataPreparation {

  final int SRIDNumber=4326;

  /** name of the all roads table */
  final String allRoadsTableName="all_roads";

  /** number of tables */
  int numOfTables;

  /** names of each table(schema + table) */
  String[][] namesOfTables;

  /** schema in which we will put data */
  String schemaName;

  /**
   * SRID number of adequate SRS with meters measure and that contains the
   * current area
   */
  final int SRIDMeters=32633;

  public RoutingDataPreparation(int numOfTables, String[][] namesOfTables,
    String schemaName) {
    this.numOfTables=numOfTables;
    this.namesOfTables=namesOfTables;
    this.schemaName=schemaName;
  }

  /**
   * transforms geometries (if necessary) from MultiLinestring type into
   * Linestring type
   * 
   * @return error type if it happens
   */
  public String transformMultiLinestring2Linestring(Statement st) {
    for (int i=0; i<numOfTables; i++) {
      try {

        ResultSet rs=st
          .executeQuery("SELECT type FROM geometry_columns WHERE f_table_schema='"
            +namesOfTables[i][0]+"' AND f_table_name='"+namesOfTables[i][1]+"'");

        /** checking if tables are in geometry_columns table */
        if (rs.next()) {
          String geometryType=rs.getString(1).trim();
          /** checking whether the table has to be transformed */
          if (geometryType.equals("MULTILINESTRING")) {
            st.execute("SELECT multilinestring2linestring('"
              +namesOfTables[i][0]+"', '"+namesOfTables[i][1]+"')");
          }
          /** checking if table's geometry is invalid */
          else if (!geometryType.equals("LINESTRING")) {
            return ("Error : Geometry in table "+namesOfTables[i][0]+"."
              +namesOfTables[i][1]+" is not MULTILINESTRING nor LINESTRING type");
          }
        } else
          return ("Error : No such table in geometry columns:"
            +namesOfTables[i][0]+"."+namesOfTables[i][1]);

      } catch (SQLException e) {
        return e.toString();
      }
    }
    return null;
  }

  /**
   * joins all lines with same names, that are not one way streets and which are
   * connected end to start, creates tables with names of original tables with
   * supplementary part "_fixed"
   * 
   * @return error type if it happens
   */
  public String collectAdequateSegments(Statement st) {
    try {
      for (int i=0; i<numOfTables; i++) {
        st.execute("SELECT collect_adequate_segments('"+namesOfTables[i][0]
          +"', '"+namesOfTables[i][1]+"', '"+schemaName+"')");
      }
      /**
       * updating information about road tables, setting schema name to
       * <code>schemaName</code> and adding supplementary part "_fixed" on
       * original table
       */
      for (int i=0; i<numOfTables; i++) {
        namesOfTables[i][0]=schemaName;
        namesOfTables[i][1]=namesOfTables[i][1]+"_fixed";
      }
      return null;

    } catch (SQLException e) {
      return e.toString();
    }
  }

  /**
   * @return error type if it happens
   */
  public String divideSegments(Statement st) {
    try {
      String returnString=createAllRoadsTable(st);
      if (returnString!=null) {
        return returnString;
      }

      /** creating table of crossroads */
      st.execute("SELECT get_vertexes_by_multiple_points('public', '"
        +allRoadsTableName+"')");

      ResultSet rs=st.executeQuery("SELECT divide_segments('public', '"
        +allRoadsTableName+"')");

      if (rs.next()) {
        if (rs.getInt(1)!=0)
          return ("Error - null geometries in function divide_segments!");
      } else {
        return ("Error with function divide_segments!");
      }

      /** dropping all roads table and crossroads table */
      st.execute("SELECT drop_geom_table('public', '"+allRoadsTableName
        +"', 'geom')");
      st.execute("SELECT drop_geom_table('public', 'vertexes', 'geom')");

      return null;

    } catch (SQLException e) {
      return e.toString();
    }
  }

  /**
   * checks whether all vertexes are reachable
   * 
   * @return error type if it happens
   */
  public String findUnreachableVertexes(Statement st, int startIndex) {
    String percentageOfUnreachableData;
    try {
      String returnString=createAllRoadsTable(st);
      if (returnString!=null) {
        return returnString;
      }

      /** creating table of vertexes */
      st.execute("SELECT get_vertexes_by_start_end('public', '"
        +allRoadsTableName+"', 'vertexes')");

      /** searching for unreachable vertexes */
      ResultSet rs=st
        .executeQuery("SELECT find_unreachable_vertexes('public', '"
          +allRoadsTableName+"', 'vertexes', "+startIndex+")");

      if (rs.next()) {
        percentageOfUnreachableData=rs.getString(1);
      } else {
        return ("Error with function find_unreachable_vertexes!");
      }

      /** dropping all roads table and vertexes table */
      st.execute("SELECT drop_geom_table('public', '"+allRoadsTableName
        +"', 'geom')");
      st.execute("SELECT drop_geom_table('public', 'vertexes', 'geom')");
      return percentageOfUnreachableData;

    } catch (SQLException e) {
      return e.toString();
    }
  }

  /**
   * create necessary tables for routing
   * 
   * @return error type if it happens
   */
  public String createRoutingGraph(Statement st) {
    try {
      /** creating table of roads */
      st
        .execute("CREATE TABLE "
          +schemaName
          +"."
          +allRoadsTableName
          +"( gid serial CONSTRAINT roads_key PRIMARY KEY, label character varying, level integer, table_gid integer, one_way boolean)");
      st.execute("SELECT addGeometryColumn('"+schemaName+"', '"
        +allRoadsTableName+"', 'geom',"+SRIDNumber+", 'LINESTRING', 2)");
      st.execute("CREATE INDEX "+schemaName+"_"+allRoadsTableName
        +"_geom_idx ON "+schemaName+"."+allRoadsTableName+" USING GIST (geom)");
      st.execute("CREATE INDEX "+schemaName+"_"+allRoadsTableName
        +"_primary_index ON "+schemaName+"."+allRoadsTableName
        +" USING BTREE (gid)");

      /** filling tables by levels */
      for (int i=0; i<numOfTables; i++) {
        st.execute("INSERT INTO "+schemaName+"."+allRoadsTableName
          +"(label, geom, level, table_gid) SELECT label, geom, "+(i+1)
          +", gid FROM "+namesOfTables[i][0]+"."+namesOfTables[i][1]);
        st.execute("SELECT get_vertexes_by_start_end('"+schemaName+"', '"
          +allRoadsTableName+"', 'vertexes_"+(i+1)+"_lev')");
        st.execute("SELECT create_edges_table('"+schemaName+"', '"
          +allRoadsTableName+"', 'edges_"+(i+1)+"_lev', "+SRIDMeters
          +", 'vertexes_"+(i+1)+"_lev')");
      }
      return null;

    } catch (SQLException e) {
      return e.toString();
    }
  }

  /**
   * create table which contains all input roads
   * 
   * @return error type if it happens
   */
  private String createAllRoadsTable(Statement st) {
    try {
      /** creating table */
      st
        .execute("CREATE TABLE "
          +allRoadsTableName
          +"( gid serial CONSTRAINT roads_key PRIMARY KEY,label character varying, table_name character varying, table_gid integer, one_way boolean)");
      st.execute("SELECT addGeometryColumn('"+allRoadsTableName+"', 'geom',"
        +SRIDNumber+", 'LINESTRING', 2)");
      st.execute("CREATE INDEX "+allRoadsTableName+"_geom_idx ON "
        +allRoadsTableName+" USING GIST (geom)");
      /** filling table with data */
      for (int i=0; i<numOfTables; i++) {
        st
          .execute("INSERT INTO "
            +allRoadsTableName
            +"(label, geom, table_name, table_gid, one_way) select label, geom, '"
            +namesOfTables[i][0]+"."+namesOfTables[i][1]
            +"', gid, one_way from "+namesOfTables[i][0]+"."
            +namesOfTables[i][1]);
      }
      return null;

    } catch (SQLException e) {
      return e.toString();
    }
  }

  /**
   * Vacuum road tables
   */
  public String vacuumRoadTables(Statement st) {
    for (int i=0; i<numOfTables; i++) {
      String e=vacuumTable(st, namesOfTables[i][0]+"."+namesOfTables[i][1]);
      if (e!=null) return e;
    }
    return null;
  }

  /**
   * Vacuum routing graph tables
   */
  public String vacuumRoutingGraph(Statement st) {
    String e=vacuumTable(st, schemaName+"."+allRoadsTableName);
    if (e!=null) return e;
    for (int i=0; i<numOfTables; i++) {
      e=vacuumTable(st, schemaName+".edges_"+(i+1)+"_lev");
      if (e!=null) return e;
      e=vacuumTable(st, schemaName+".vertexes_"+(i+1)+"_lev");
      if (e!=null) return e;
    }
    return null;
  }

  /**
   * Do vacuum on specified table
   * 
   * @param tableName - name of the table
   * @return error type if it happens
   */
  public String vacuumTable(Statement st, String tableName) {
    try {
      st.execute("VACUUM FULL ANALYZE "+tableName);

      return null;

    } catch (SQLException e) {
      return e.toString();
    }
  }
}
