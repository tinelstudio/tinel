TineL, 2009-10-11

How to create database schema?

1. Install PostgreSQL database (tested on version 8.3.7).
2. Install PostGIS add-on (tested on version 1.3.6).
3. Use attached pgAdmin manager to create new database. Use "template_postgis"
   template database, which automatically adds two additional tables
   (geometry_columns and spatial_ref_sys).
4. Edit properties in Groovy script src/main/java/DbSchemaCreator.groovy to set
   appropriate access to the database (location, username, password).
5. Run Groovy script src/main/java/DbSchemaCreator.groovy to create schema.
6. Edit properties in db.properties to set appropriate access to the database
   (location, username, password).
