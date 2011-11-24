
package net.tinelstudio.gis.model.old.routing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Savepoint;
import java.sql.Statement;

/**
 * Main class for routing data preparation</b> tables have to have following
 * columns(integer/serial gid, Geometry geom, character varying/character label,
 * boolean one_way</b> Input argument args[0] means location of the file with
 * names of schemas and tables(one line for each table with priority from
 * biggest to smallest, schema name and table name are delimited with '.', )
 * example: </b> public;eazvceste routing_schema;ebzsceste</b> PostGIS has to
 * have functions from file "vse funkcije.sql"</b> beware to set global
 * variables <code>url</code> and <code>schemaName</code>
 * 
 * @author Ivan Fucak
 * @author TineL
 */
public class RoutingDataPreparationControl {

  /** URL of PostGIS database */
  static String url="jdbc:postgresql://localhost/old-gis";

  /** PostGIS user name */
  static String userName="gis";

  /** PostGIS password */
  static String password="gis";

  /*
   * Next properties reflects to the domain objects, so don't change them,
   * unless you know what you are doing.
   */

  /** name of the schema in which data will be put */
  // static String schemaName="routing_schema";
  static String schemaName="routing";

  /** Schema & table name of road tables to prepare. */
  static String[][] nameOfTheTables={ //
  {"public", "eazvceste"}, // Slovenia major roads
    {"public", "ebzsceste2"}, // Slovenia middle roads
    {"public", "eczmceste"}, // Slovenia minor roads
  };

  /** maximal number of road tables */
  static int maxNumOfRoads=10;

  /** actual number of roads */
  static int numOfRoads;

  public static void main(String[] args) throws IOException {
    /** possible error is written in this variable */
    String errorCheck=null;
    Connection conn;

    try {
      Class.forName("org.postgresql.Driver");
      conn=DriverManager.getConnection(url, userName, password);
      /** transaction has to be committed */
      conn.setAutoCommit(false);
      Statement st=conn.createStatement();

      BufferedReader in=new BufferedReader(new InputStreamReader(System.in));

      /** reading table names from file */
      // String[][] nameOfTheTables = readFromFile(args[0]);
      /** continuing only if table names are retrieved */
      if (nameOfTheTables!=null) {
        RoutingDataPreparation prepareData=new RoutingDataPreparation(
          nameOfTheTables.length, nameOfTheTables, schemaName);
        /** 1. transforming MultiLineStrings to LineStrings */
        System.out
          .println("1. Transforming possible MultiLineStrings to LineStrings");
        errorCheck=prepareData.transformMultiLinestring2Linestring(st);
        if (errorCheck!=null) {
          throw new Exception(errorCheck);
        }

        /** 2. fixing geometry data */
        System.out.println("2. Fixing geometry data");
        errorCheck=prepareData.collectAdequateSegments(st);
        if (errorCheck!=null) {
          throw new Exception(errorCheck);
        }
        errorCheck=prepareData.divideSegments(st);
        if (errorCheck!=null) {
          throw new Exception(errorCheck);
        }

        /** save point before searching for unreachable data */
        Savepoint savePoint=conn.setSavepoint();

        System.out
          .println("Do you wish to check whether all data is reachable(y/n)?");
        if (in.readLine().startsWith("y")) {
          /** + Checking whether all data is reachable */
          System.out.println("+ Checking whether all data is reachable");
          errorCheck=prepareData.findUnreachableVertexes(st, 1);
          System.out.println(errorCheck);
          try {
            System.out.println("Unrechable data is about "
              +(Double.parseDouble(errorCheck)*100)+" % of all data");
            System.out
              .println("Data will be put in table public.unreachable_crossroads");

          } catch (NumberFormatException e) {
            /** in case of error with finding unreachable vertexes */
            System.out.println(errorCheck);
            conn.rollback(savePoint);
            System.out
              .println("Continue with routing graph preparation? (y/n)");
            /**
             * checking whether the user wants to save changes with the data
             */
            if (!in.readLine().startsWith("y")) {
              System.out.println("Save fixed data? (y/n)");
              if (in.readLine().startsWith("y")) {
                /** committing changes and closing connection */
                conn.commit();
                conn.close();
                System.out.println("Fixed data was saved.");
              }
            }
            throw new Exception(errorCheck);
          }
        }

        // Using RoadTranslator directly on fixed roads

        // System.out.println("Do you want to create routing graph (y/n)?");
        // if (in.readLine().startsWith("y")) {
        //
        // /** 3. Creating routing graph */
        // System.out.println("3. Creating routing graph");
        // errorCheck=prepareData.createRoutingGraph(st);
        // if (errorCheck!=null) {
        // throw new Exception(errorCheck);
        // }
        // }

        /** committing changes and closing connection */
        conn.commit();
        conn.close();

        // conn=DriverManager.getConnection(url, userName, password);
        // conn.setAutoCommit(true);
        // st=conn.createStatement();
        // /** vacuum */
        // errorCheck=prepareData.vacuumRoadTables(st);
        // if (errorCheck!=null) {
        // throw new Exception(errorCheck);
        // }
        //
        // errorCheck=prepareData.vacuumRoutingGraph(st);
        // if (errorCheck!=null) {
        // throw new Exception(errorCheck);
        // }
        // conn.close();
        System.out.println("Routing data preparation finished!");
      }
    } catch (Exception e) {
      System.out.println("Error : "+e);
    }
  }

  /**
   * @param filePath - location of file with table names
   * @return table names (schema + table name)
   */
  private static String[][] readFromFile(String filePath) {
    String[][] nameOfTheTables=new String[maxNumOfRoads][2];
    try {
      BufferedReader reader=new BufferedReader(new InputStreamReader(
        new FileInputStream(filePath)));

      String line;

      while ((line=reader.readLine())!=null) {
        int indexOfFullStop=line.indexOf(".");
        nameOfTheTables[numOfRoads][0]=line.substring(0, indexOfFullStop);
        nameOfTheTables[numOfRoads][1]=line.substring(indexOfFullStop+1, line
          .length());
        numOfRoads++;
      }

      return nameOfTheTables;

    } catch (Exception e) {
      System.out.println("Error : "+e);
      return null;
    }
  }

  // @Test
  // public void testTransformMultilinestring2Linestring() {
  // try {
  // Class.forName("org.postgresql.Driver");
  // Connection conn=DriverManager.getConnection(url, userName, password);
  // Statement st=conn.createStatement();
  //
  // String[][] nameOfTheTables=new String[1][2];
  // // table not in the geometry_columns
  // nameOfTheTables[0][0]="public";
  // nameOfTheTables[0][1]="sasd";
  // RoutingDataPreparation prepareData=new RoutingDataPreparation(1,
  // nameOfTheTables, "public");
  // assertEquals(prepareData.transformMultiLinestring2Linestring(st)
  // .startsWith("Error : No such table"), true);
  //
  // // table has invalid geometry
  // nameOfTheTables[0][0]="public";
  // nameOfTheTables[0][1]="dead_ends";
  // prepareData=new RoutingDataPreparation(1, nameOfTheTables, "public");
  // assertEquals(prepareData.transformMultiLinestring2Linestring(st)
  // .startsWith("Error : Geometry"), true);
  //
  // } catch (ClassNotFoundException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  //
  // } catch (SQLException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
}
