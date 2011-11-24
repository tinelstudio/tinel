/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

import java.util.Map;

/**
 * The basic SQL query builder.
 * 
 * @author TineL
 */
class SqlBuilder {
  
  String schemaName="public"
  int srid=4326
  String shapeColumnName="shape"
  int shapeDim=2
  
  String transactional(String sql) {
    """
-- Created by Groovy script
-- Compatible with PostgreSQL
-- ${new Date()}

BEGIN;

$sql

COMMIT;
    """
  }
  
  String createTable(Map<String, String> map) {
    String a="""
-- Table: ${map.name}
    """
    
    if (map.drop) {
      a+="""
-- Function: For cascade dropping a table if exists
    CREATE OR REPLACE FUNCTION drop_cascaded(character varying)
      RETURNS BOOLEAN AS \$\$
    BEGIN
      IF EXISTS(SELECT relname FROM pg_class WHERE relname=\$1) THEN
        EXECUTE 'DROP TABLE ' || \$1 || ' CASCADE';
        RETURN true;
      ELSE
        RETURN false;
      END IF;
    END;
    \$\$ LANGUAGE plpgsql;

  SELECT drop_cascaded('${map.name}');
  """
    }
    
    a+="""
  CREATE TABLE ${map.name} ();
  """
    
    map.columns.each { a+="""
  ALTER TABLE ${map.name} ADD COLUMN $it;
  """ }
    
    if (map.pk) {
      a+="""
  ALTER TABLE ${map.name}
    ADD CONSTRAINT ${map.name}_pkey PRIMARY KEY (${map.pk});
  """
    }
    
    if (map.unique) {
      String uniqName=map.unique.replace(" ", "").replace(",", "_")
      a+="""
  ALTER TABLE ${map.name}
    ADD CONSTRAINT ${map.name}_${uniqName}_key UNIQUE (${map.unique});
  """
    }
    
    map.indices.each { a+="""
  CREATE INDEX ${map.name}_${it}_idx
  ON ${map.name} ($it);
  """ }
    
    map.geoColumns.each { k, v -> a+="""
  SELECT AddGeometryColumn (
    '${map.name}',
    '$k',
    $srid,
    '$v',
    $shapeDim
  );

  CREATE INDEX ${map.name}_${k}_idx
  ON ${map.name}
  USING GIST ($k);
  """ }
    
    return a
  }
  
  String createForeignKey(Map<String, String> map) {
    """
-- Foreign key: ${map.table}.${map.column} -> ${map.reference}

      ALTER TABLE ${map.table}
        ADD CONSTRAINT ${map.table}_${map.column}_${map.reference}_fkey
        FOREIGN KEY (${map.column})
        REFERENCES ${map.reference};
      """
  }
  
  String addTableColumn(Map<String, String> map) {
    """
    -- Table: ${map.table} Add Column ${map.column}
    
    ALTER TABLE ${map.table}
      ADD COLUMN ${map.column};
    """
  }
  
  String dropTableCascaded(String name) {
    """
-- Function: For cascade dropping a table if exists
    CREATE OR REPLACE FUNCTION drop_cascaded(character varying)
      RETURNS BOOLEAN AS \$\$
    BEGIN
      IF EXISTS(SELECT relname FROM pg_class WHERE relname=\$1) THEN
        EXECUTE 'DROP TABLE ' || \$1 || ' CASCADE';
        RETURN true;
      ELSE
        RETURN false;
      END IF;
    END;
    \$\$ LANGUAGE plpgsql;

  SELECT drop_cascaded('$name');
  """
  }
}