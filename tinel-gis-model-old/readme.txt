TineL, 2009-10-12

How to convert your old data to the new domain model?

1. Rename all geometry columns in all tables in the old DB to name 'geom'.
2. Update 'f_geometry_column' attributes in the 'geometry_columns' table to read
   'geom'.
3. Add boolean type attribute named 'one_way' to all street type tables. Set
   them to NON-null and defaults to FALSE. Can take few moments to complete.
4. Run SQL script sql/vse_funkcije.sql in the old DB to add special SQL
   functions for street repair.
5. Create new schema aside 'public' named 'routing'.
6. Open net.tinelstudio.gis.model.old.routing.RoutingDataPreparationControl, set
   some DB properties (Java fields) and run it to actually repair streets.
   First, it will transform MultiLineStrings to LineString - can take few
   moments; then it will fix the geometry data (nodes, crossroads, etc.) - can
   take half an hour or so; then it will ask you to check whether all data is
   reachable - you can safely skip this part since it takes several hours to
   complete.
   All those changes will be reflected in new street tables with '_fixed' suffix
   in 'routing' schema.
7. Take a look at domain objects in net.tinelstudio.gis.model.old.domain and
   rename them or create new one, to reflect the tables in the old DB.
8. Run net.tinelstudio.gis.model.old.util.DuplicateOldStreetFinder tool to
   check for any duplicate streets. If they exist, delete them with
   net.tinelstudio.gis.model.old.util.OldStreetDeleter. Can take some minutes to
   complete.
9. Create new DB to which the data will be converted. Please see the readme.txt
   in tinel-gis-model project.
10.Take a look at DB connection properties in oldDb.properties for the old DB
   and db.properties for the new DB. Both files are in src/test/resources dir.
11.You are ready to convert addresses. Try using JUnit test
   net.tinelstudio.gis.model.old.util.CshlCsvTranslator. It converts addresses
   from CSV file.
12.You are ready to convert streets. Try using JUnit test
   net.tinelstudio.gis.model.old.util.OldStreetTranslator. It converts streets
   from the old DB.
13.You are ready to convert buildings. Try using JUnit test
   net.tinelstudio.gis.model.old.util.OldBuildingTranslator. It converts
   buildings from the old DB.
14.In the end, be sure to fully vacuumize the new DB.
