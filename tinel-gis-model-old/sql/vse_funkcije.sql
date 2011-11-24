/**
 * FUNCTION drop_geom_table removes table "'geom_schema'.'geom_table'" 
 * with geometry attribute 'geom_attribute'
 */
CREATE OR REPLACE FUNCTION drop_geom_table(geom_schema character varying, geom_table character varying, geom_attribute character varying)
  RETURNS void AS
$BODY$
BEGIN
	EXECUTE $q$ SELECT DropGeometryColumn('$q$ || geom_schema || $q$', '$q$ || geom_table || $q$', '$q$ || geom_attribute || $q$')$q$;
        EXECUTE $q$ DROP TABLE $q$ || geom_schema || $q$.$q$ || geom_table;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;


/**
 * FUNCTION multilinestring2linestring creates table 
 * "'geom_schema'.'geom_table'" with geom type LINESTRING from table 
 * "'geom_schema'.'geom_table_fixed'" which has geom type MULTILINESTRING
 */
CREATE OR REPLACE FUNCTION multilinestring2linestring(geom_schema character varying, geom_table character varying)
  RETURNS void AS
$BODY$
BEGIN
	--creating temporary table equal to input data...
	EXECUTE $q$ CREATE TEMP TABLE temp_road_data_table AS SELECT * FROM $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) ;
	--..., removing geometry...
	EXECUTE $q$ ALTER TABLE temp_road_data_table DROP COLUMN geom$q$;
	--..and renaming gid column
	EXECUTE $q$ ALTER TABLE temp_road_data_table RENAME COLUMN gid TO old_gid$q$;

	--creating another table which will be possibly extendted due to multi lines in a single row
	EXECUTE $q$ CREATE TABLE $q$ || quote_ident(geom_schema) || $q$.temp_road_data_table2(gid serial, gid_temp integer)$q$ ;
	EXECUTE $q$ SELECT addGeometryColumn($q$ || quote_literal(geom_schema) || $q$, 'temp_road_data_table2', 'geom',4326, 'LINESTRING', 2)$q$;
	--inserting data
	EXECUTE $q$INSERT INTO $q$ || quote_ident(geom_schema) || $q$.temp_road_data_table2(gid_temp, geom) SELECT gid, GeometryN(geom, generate_series(1,numGeometries(geom))) AS geom FROM $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table);
	
	--creating final table 'geom_table'_fixed
	EXECUTE $q$ CREATE TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$_fixed AS (SELECT * FROM $q$ || quote_ident(geom_schema) || $q$.temp_road_data_table2, temp_road_data_table WHERE gid_temp=old_gid)$q$ ;

	--droping old table and renaming new one
	EXECUTE $q$ SELECT drop_geom_table($q$ || quote_literal(geom_schema) || $q$, $q$ || quote_literal(geom_table) || $q$, 'geom')$q$;
	EXECUTE $q$ ALTER TABLE $q$ || geom_schema || $q$.$q$ || quote_ident(geom_table) || $q$_fixed RENAME TO $q$ || geom_table;

	
	--deleting doubled old gid
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ DROP COLUMN gid_temp$q$;
	--updating data in table geometry_columns and adding constraints
	EXECUTE $q$ UPDATE geometry_columns SET f_table_name = '$q$ || quote_ident(geom_table) || $q$' WHERE f_table_name='temp_road_data_table2'$q$;
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ ADD CONSTRAINT enforce_dims_geom CHECK (ndims(geom) = 2)$q$;
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ ADD CONSTRAINT enforce_geotype_geom CHECK (geometrytype(geom) = 'LINESTRING'::text OR geom IS NULL)$q$;
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ ADD CONSTRAINT enforce_srid_geom CHECK (srid(geom) = 4326)$q$;
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ ADD PRIMARY KEY (gid)$q$;
	--create indexes
	EXECUTE $q$ CREATE INDEX $q$ || quote_ident(geom_table) || $q$_geom_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(geom_table) || $q$ USING GIST (geom)$q$;


	EXECUTE $q$ DROP TABLE $q$ || quote_ident(geom_schema) || $q$.temp_road_data_table2$q$;
	DROP TABLE temp_road_data_table;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;


/**
 * FUNCTION get_vertexes_by_start_end creates table of vertexes 
 * ("'geom_schema'.'vertexes_table'"), in which are start and end 
 * points of "'geom_schema'.'roads_table'"
 */
CREATE OR REPLACE FUNCTION get_vertexes_by_start_end(geom_schema character varying, roads_table character varying, vertexes_table character varying)
  RETURNS void AS
$BODY$
BEGIN
	--creating new table vertexes
	EXECUTE $q$ CREATE TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(vertexes_table) || $q$ ( id serial CONSTRAINT $q$ || quote_ident(vertexes_table) || $q$_key PRIMARY KEY )$q$;
        EXECUTE $q$ SELECT addGeometryColumn($q$ || quote_literal(geom_schema) || $q$,$q$ || quote_literal(vertexes_table) || $q$, 'geom', 4326, 'POINT', 2) $q$;
	EXECUTE $q$ CREATE INDEX $q$ || quote_ident(vertexes_table) || $q$_geom_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(vertexes_table) || $q$ USING GIST (geom)$q$;

	--creating temp table with all start and end points
	EXECUTE $q$ CREATE TEMP TABLE temp_vertexes AS ((SELECT ST_StartPoint(R.geom) AS geom FROM $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ R) UNION (SELECT ST_EndPoint(R.geom) AS geom FROM $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ R))$q$;

	--inserting unique data into the table
	EXECUTE $q$ INSERT INTO $q$ || quote_ident(geom_schema) || $q$.$q$ || quote_ident(vertexes_table) || $q$(geom) (SELECT DISTINCT geom FROM temp_vertexes)$q$;

	drop table temp_vertexes;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;


/**
 * FUNCTION get_vertexes_by_multiple_points creates table of vertexes 
 * ('vertexes'), in which are multiple points (in this case means that they 
 * appear in more than one line) in table "'roads_schema'.'roads_table'"
 */
CREATE OR REPLACE FUNCTION get_vertexes_by_multiple_points(roads_schema character varying, roads_table character varying)
  RETURNS void AS
$BODY$
DECLARE
	--record of a line
        line record;
        --record of one element (point) of line
	element record;
BEGIN
	-- creating temporary table 'vertex_temp'
        CREATE TABLE vertexes_temp ( id serial CONSTRAINT vertexes_temp_key PRIMARY KEY );
        EXECUTE $q$ SELECT addGeometryColumn('vertexes_temp', 'geom', 4326, 'POINT', 2) $q$;
        CREATE INDEX vertexes_temp_geom_idx ON vertexes_temp USING GIST (geom);

        --getting all points from lines (roads) and putting them into table 'vertex_temp'
        FOR line IN EXECUTE $q$SELECT geom AS geom, ST_NumPoints(geom) AS number_points FROM $q$ 
			|| quote_ident(roads_schema) || $q$.$q$ || quote_ident(roads_table) LOOP
		FOR i IN 1..line.number_points LOOP
			SELECT INTO element ST_PointN(line.geom,i) AS point;
			INSERT INTO vertexes_temp (geom) VALUES (element.point);
		END LOOP;
        END LOOP;
     
	--creating final table and taking only those which are multiple points
	CREATE TABLE vertexes ( id serial CONSTRAINT vertexes_key PRIMARY KEY );
        EXECUTE $q$ SELECT addGeometryColumn('vertexes', 'geom', 4326, 'POINT', 2) $q$;
	CREATE INDEX vertexes_geom_idx ON vertexes USING GIST (geom);
	INSERT INTO vertexes(geom) SELECT DISTINCT ON (v1.geom) v1.geom FROM vertexes_temp v1, vertexes_temp v2 WHERE v1.geom~=v2.geom AND v1.id!=v2.id;

	--deleting temporary table
	PERFORM drop_geom_table('public', 'vertexes_temp', 'geom');
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;

/**
 * FUNCTION collect_adequate_segments creates new table 
 * "'destination_schema'.'roads_table'" from table of all roads 
 * ("'roads_schema'.'roads_table'"), replacing lines with same name 
 * with a single line and consecutively changing rows.
 * restrictions are that lines have to connected (end to start) 
 * and not to be one way streets
 */
CREATE OR REPLACE FUNCTION collect_adequate_segments(roads_schema character varying, roads_table character varying, destination_schema character varying)
  RETURNS void AS
$BODY$
DECLARE
        row record;
        --number of linestrings in a multilinestring
	num_of_linestrings record;
	--current label
	label_saved character varying;
BEGIN
	--creating temporary table which will become final table
        EXECUTE $q$ CREATE TABLE segments_temp_table ( gid serial CONSTRAINT $q$ || roads_table || $q$_fixed_key PRIMARY KEY, label character varying, one_way boolean)$q$;
        EXECUTE $q$ SELECT addGeometryColumn('segments_temp_table', 'geom', 4326, 'LINESTRING', 2) $q$;
        EXECUTE $q$ CREATE INDEX $q$ || roads_table || $q$_fixed_geom_idx ON segments_temp_table USING GIST (geom)$q$;

        --collecting adequate lines- same name and not one way street
	FOR row IN EXECUTE $q$ SELECT label,(ST_LineMerge(COLLECT(geom))) AS geom FROM $q$ || roads_schema || $q$.$q$ || roads_table || $q$ WHERE one_way=false GROUP BY label $q$ LOOP

		--due to possible problems with empty label
		IF (row.label IS NULL) THEN
			label_saved='';
		ELSE
			label_saved=row.label; 
		END IF;

		--checking num_of_linestring 
		SELECT INTO num_of_linestrings NumGeometries(row.geom) as num;
		IF (num_of_linestrings.num IS NULL) THEN
		--if result geometry is LINESTRING
			INSERT INTO segments_temp_table (geom, label, one_way) VALUES (row.geom, label_saved, false);
		ELSE
		--if result geometry is MULTILINESTRING
			FOR i in 1..num_of_linestrings.num LOOP
				INSERT INTO segments_temp_table (geom, label, one_way) VALUES ((SELECT(GeometryN(row.geom,i))),label_saved, false);
			END LOOP;
		END IF;
        END LOOP;

        --inserting one_way streets
        FOR row IN EXECUTE $q$ SELECT geom, label, one_way FROM $q$ || roads_schema || $q$.$q$ || roads_table || $q$ WHERE one_way=true$q$ LOOP
		INSERT INTO segments_temp_table (geom, label, one_way) VALUES (row.geom, row.label, row.one_way);
	END LOOP;
	
	--setting sequence,schema and name of the table
	EXECUTE $q$ ALTER TABLE segments_temp_table_gid_seq RENAME TO $q$ || roads_schema || $q$_$q$ || roads_table || $q$_fixed_gid_seq$q$;
        EXECUTE $q$ ALTER TABLE segments_temp_table SET SCHEMA $q$ || destination_schema;
        EXECUTE $q$ ALTER TABLE $q$ || destination_schema || $q$.segments_temp_table RENAME TO $q$ || roads_table || $q$_fixed$q$;
        --updating geometry_columns table
        EXECUTE $q$ UPDATE geometry_columns SET f_table_schema='$q$ || destination_schema || $q$', f_table_name = '$q$ || roads_table || $q$_fixed' WHERE f_table_schema='public' AND f_table_name='segments_temp_table'$q$;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;




/**
 * FUNCTION divide_segments divides corresponding segments in roads
 * from table "'roads_schema'.'roads_table'" and putting segments into suitable 
 * roads table
 * returns number of empty geometry - has to be 0
 */
CREATE OR REPLACE FUNCTION divide_segments(roads_schema character varying, roads_table character varying)
  RETURNS integer AS
$BODY$
DECLARE
	--record of crossroad and line in which it is
        pointslines record;
        --record used for counting the number of points in line
	row record;
	
	--true if current crossroad is equal to the first point in line
	same_as_first_point boolean;
	--distance from the first point of the line and crossroad
	part_distance float;
	--current line of all roads table in loop
	current_line Geometry;
	--index of crossroad in line
	num float;

	--record of wrong crossroad
	row2 record;

        --current name of the roads table
	last_table_name character varying;
	--current gid in the roads table
        last_table_gid integer;

	--id of previous road in all roads table "'roads_schema'.'roads_table'"
	last_gid integer:=0;
	--previous index of crossroad in road in all roads table
	last_value float:=0.0;
	--number of empty geometries
	null_data integer:=0;

BEGIN
	--1. CREATING TABLE WRONG CROSSROADS
	
	--table 'wrong_crossroads' 
	--id is identifier from vertexes 
	--gid is identifier "'roads_schema'.'roads_table'"
	--turn is position of point in line
        CREATE TABLE wrong_crossroads ( cid serial CONSTRAINT wrong_crossroads_key PRIMARY KEY, id integer, gid integer, turn float);

        FOR pointslines IN EXECUTE $q$ SELECT V.geom as point, id, R.geom as line,gid FROM vertexes V, $q$ 
			|| quote_ident(roads_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ R WHERE ST_Contains(R.geom, V.geom)$q$  LOOP

		current_line=pointslines.line;
		--counting the number of points in line
		SELECT INTO row ST_NumPoints(current_line) as number_points;

		--considering the possibility that a line can cross first point more than once
		--value would be 0.0, which is index of the first point of the line and which is wrong
		IF((SELECT ST_line_locate_point(pointslines.line, pointslines.point))=0.0) THEN
			same_as_first_point:=true;
		ELSE
			same_as_first_point:=false;
		END IF;
 
		part_distance:=0.0;

		--looping without margins through points of the line
		FOR i IN 2..(row.number_points-1) LOOP
			--if crossroad is equal to first point, part_distance and current_line have to be corrected
			IF(same_as_first_point = true) THEN
				--adding distance from first point to the crossroad to part_distance
				part_distance:= part_distance + (SELECT length2D(line_substring(current_line,0.0,ST_line_locate_point(current_line,ST_PointN(pointslines.line,i)))));
				--current line excludes part from first point to crossroad
				current_line:=line_substring(current_line,ST_line_locate_point(current_line,ST_PointN(pointslines.line,i)),1.0);
				--if that is the only occurence of that crossroad, heading out of loop
				IF(((SELECT ST_line_locate_point(current_line, pointslines.point)) <= 0.0) AND ((SELECT ST_line_locate_point(current_line, pointslines.point))>=1.0)) THEN
					EXIT;
				END IF;
				--setting same_as_first_point back to false
				same_as_first_point:=false;
			END IF;
			--checking if crossroad is explicit point in line
			IF (ST_PointN(pointslines.line,i)~=pointslines.point)
			THEN
				--crossroad is found - setting data for possible more occurence of current crossroad
				same_as_first_point:=true;
				--calculating index of the crossroad
				num:=(part_distance + (SELECT length2D(line_substring(current_line,0.0,ST_line_locate_point(current_line,pointslines.point)))))/(SELECT length2D(pointslines.line));
				--inserting wrong crossroad with necessary data
				INSERT INTO wrong_crossroads(id,gid, turn) VALUES (pointslines.id, pointslines.gid, num);
				END IF;
			END LOOP;
        END LOOP;

        --2. DIVIDING SEGMENTS
        --sorting table of wrong crossroads by gid("'roads_schema'.'roads_table'") and adding additional attributes from "'roads_schema'.'roads_table'"
	FOR row2 IN EXECUTE $q$ SELECT C.turn as turn, C.gid as gid, R.table_gid as table_gid , R.table_name AS table_name FROM $q$ 
			|| quote_ident(roads_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ R, wrong_crossroads C  WHERE C.gid=R.gid ORDER BY C.gid, turn$q$ LOOP
		--checking if all segments have already been excluded from previous road and it's not the start of loop
		IF((row2.gid!=last_gid)and(last_gid!=0))THEN
			BEGIN
				--last segment for previous road (from index last_value to index 1.0)
				EXECUTE $q$ UPDATE $q$ || last_table_name || $q$ SET geom = ST_line_substring(G.geom , $q$ || last_value|| $q$, 1.0 ) FROM $q$ 
				|| quote_ident(roads_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ G WHERE G.gid=$q$ || last_gid || $q$ AND $q$ || last_table_name || $q$.gid=$q$ || last_table_gid;
			--in case of empty geometry
			EXCEPTION 
				WHEN check_violation THEN
				null_data:=null_data + 1;
			END;
			--end of road - reseting last_value 
			last_value:=0.0;
		END IF;

		BEGIN
			--excluding segment from index last_value to index of crossroad
			EXECUTE $q$ INSERT INTO $q$ || row2.table_name || $q$ (geom,label, one_way) (SELECT ST_line_substring(G.geom , $q$ || last_value || $q$, $q$ || 
			row2.turn || $q$), G.label, G.one_way FROM $q$ || quote_ident(roads_schema) || $q$.$q$ || quote_ident(roads_table) || $q$ G WHERE G.gid=$q$ || row2.gid || $q$) $q$ ;
		EXCEPTION 
			WHEN check_violation THEN
			null_data:=null_data + 1;
		END;
		
		

		--setting the name of current corresponding roads table
		last_table_name=row2.table_name;
		--id in current corresponding roads table
		last_table_gid=row2.table_gid;
		--moving index onto last crossroad index
		last_value=row2.turn;
		--setting id of current road in all roads table
		last_gid=row2.gid;
		
        END LOOP;

        --correcting also the last segment of the last road
        BEGIN
		EXECUTE $q$ UPDATE $q$ || last_table_name || $q$ SET geom = ST_line_substring(G.geom , $q$ || last_value|| $q$, 1.0 ) FROM $q$ || quote_ident(roads_schema) || $q$.$q$ || 
		quote_ident(roads_table) || $q$ G WHERE G.gid=$q$ || last_gid || $q$ AND $q$ || last_table_name || $q$.gid=$q$ || last_table_gid;
	EXCEPTION 
		WHEN check_violation THEN
		null_data:=null_data + 1;
	END;

        --deleting table of wrong crossroads
        EXECUTE $q$ DROP TABLE wrong_crossroads $q$;
        
	RETURN null_data;

END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;



/**
 * FUNCTION find_unreachable_vertexes creates table of vertexes 
 * ('unreachable_vertexes'), which cannot be reached from vertex with gid 'start_number'
 * "'roads_schema'.'roads_table'" - lines(roads) table
 * vertexes_table - name of the vertexes table
 */
CREATE OR REPLACE FUNCTION find_unreachable_vertexes(roads_schema character varying, roads_table character varying, vertexes_table character varying, start_number integer)
  RETURNS float AS
$BODY$
DECLARE
	--record used in checking whether the start index is valid
	--also number of vertexes
	max_number record;
	--records of vertexes successors
	row record;
	--index from which the search is commenced
	start_from integer;
	--variables which are use in a standard way in a while loop
	last_id integer;
	--number of all succesors
	num_of_all_sucessors integer;
	--also used in while loop, holds gid value of current vertex
	check_number integer;
	--number of unreachable vertexes
	num_of_unreachable_vertexes integer;
BEGIN
	BEGIN
                SELECT id FROM unreachable_vertexes WHERE id=1;
                PERFORM drop_roads_table('public', 'unreachable_vertexes', 'geom');
        EXCEPTION 
                WHEN UNDEFINED_TABLE THEN
        END;

	-- checking if start index is valid, final value is written in variable last_id
	FOR max_number IN EXECUTE $q$ SELECT MAX(id) AS num FROM $q$ || vertexes_table LOOP
		EXIT;
	END LOOP;
	IF ((start_number > max_number.num) OR (start_number < 1)) THEN
		start_from:=1;
	ELSE 
		start_from:=start_number;
	END IF;

	--creating table of unreachable vertexes
	CREATE TABLE unreachable_vertexes ( id integer CONSTRAINT unreachable_vertexes_key PRIMARY KEY );
        EXECUTE $q$ SELECT addGeometryColumn('unreachable_vertexes', 'geom', 4326, 'POINT', 2) $q$;
        CREATE INDEX unreachable_vertexes_idx ON unreachable_vertexes USING GIST (geom);

	--creating table of reachable vertexes, num is gid from vertexes table
	CREATE TABLE reachable_vertexes ( id serial CONSTRAINT cand_unreachable_vertexes_key PRIMARY KEY, num integer);
	INSERT INTO reachable_vertexes (num) VALUES (start_from);
	CREATE INDEX reachable_vertexes_idx ON reachable_vertexes USING BTREE (num);

	last_id:=1; num_of_all_sucessors:=1;
	--searching all reachable vertexes
	--going through a single vertexes and checking its successors
	WHILE (last_id<=num_of_all_sucessors) LOOP
		check_number:=(SELECT num FROM reachable_vertexes WHERE id=last_id);
		--successors which are start points in line
		FOR row IN EXECUTE $q$ SELECT C2.id AS id FROM $q$ || vertexes_table || $q$ C1, $q$ || vertexes_table || $q$ C2, $q$ || quote_ident(roads_schema) || $q$.$q$ 
				|| roads_table || $q$ R WHERE C1.id=$q$ || check_number || $q$ AND C1.geom~=StartPoint(R.geom) AND C2.geom=EndPoint(R.geom) AND C2.id NOT IN (SELECT num FROM reachable_vertexes)$q$ LOOP
			INSERT INTO reachable_vertexes (num) VALUES (row.id);
			num_of_all_sucessors:=num_of_all_sucessors+1;
		END LOOP;
			
		--successors which are end points in line
		FOR row IN EXECUTE $q$ SELECT C2.id AS id FROM $q$ || vertexes_table || $q$ C1, $q$ || vertexes_table || $q$ C2, $q$ || quote_ident(roads_schema) || $q$.$q$ || roads_table || $q$ R WHERE C1.id=$q$ 
				|| check_number || $q$ AND C1.geom~=EndPoint(R.geom) AND C2.geom=StartPoint(R.geom) AND C2.id NOT IN (SELECT num FROM reachable_vertexes)$q$ LOOP
			INSERT INTO reachable_vertexes (num) VALUES (row.id);
			num_of_all_sucessors:=num_of_all_sucessors+1;
		END LOOP;
		
		last_id:=last_id+1;
	END LOOP;

	--inserting into 'unreachable_vertexes' values that aren't reached
        EXECUTE $q$ INSERT INTO unreachable_vertexes (id, geom) SELECT id, geom FROM  $q$ || vertexes_table || $q$ WHERE id NOT IN (SELECT num FROM reachable_vertexes)$q$;

	DROP TABLE reachable_vertexes;
	
        num_of_unreachable_vertexes = (SELECT COUNT(*) FROM unreachable_vertexes);
        RETURN (SELECT(CAST(num_of_unreachable_vertexes AS float))/(CAST(max_number.num AS float)));
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;



 /**
  * Find UTM (WGS84) SRID for a point (in any SRID)
  * additional function in PostGIS
  * EXAMPLE: SELECT utmzone(ST_Centroid(geom))
  */
 CREATE OR REPLACE FUNCTION utmzone(geometry)
   RETURNS integer AS
 $BODY$
 DECLARE
     geomgeog geometry;
     zone int;
     pref int;

 BEGIN
     geomgeog:=transform($1,4326);

     IF (y(geomgeog))>0 THEN
 	pref:=32600;
     ELSE
	pref:=32700;
     END IF;

     zone:=floor((x(geomgeog)+180)/6)+1;

     RETURN zone+pref;
 END;
 $BODY$ LANGUAGE 'plpgsql' IMMUTABLE
   COST 100;



/**
 * FUNCTION create_edges_table creates table of edges  ("'geom_schema'.'edges_table'"), 
 * which has simple lines (two points), lengths (line_length) etc. of current levels
 * srid_meters - SRID number of adequate SRS with meters measure and that contains the current area
 * "'roads_schema'.'roads_table'" - lines (roads) table
 * vertexes_table - name of the vertexes table on the current level
 */
CREATE OR REPLACE FUNCTION create_edges_table(geom_schema character varying, roads_table character varying, edges_table character varying, srid_meters integer, vertexes_table character varying)
  RETURNS void AS
$BODY$
BEGIN
	EXECUTE $q$ CREATE TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$ ( id serial CONSTRAINT $q$ || edges_table || $q$_key PRIMARY KEY, roads_gid integer, level integer, label character varying, start_point integer, end_point integer, line_length double precision)$q$;

	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$  ADD CONSTRAINT $q$ || edges_table || $q$_fkey FOREIGN KEY (start_point) REFERENCES $q$ || quote_ident(geom_schema) || $q$.$q$ || vertexes_table || $q$ (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION$q$; 
	EXECUTE $q$ ALTER TABLE $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$  ADD CONSTRAINT $q$ || edges_table || $q$_2fkey FOREIGN KEY (end_point) REFERENCES $q$ || quote_ident(geom_schema) || $q$.$q$ || vertexes_table || $q$ (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION$q$;

	EXECUTE $q$ CREATE INDEX $q$ || edges_table || $q$_start_point_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$ USING BTREE (start_point)$q$;
	EXECUTE $q$ CREATE INDEX $q$ || edges_table || $q$_end_point_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$ USING BTREE (end_point)$q$;
	
	EXECUTE $q$ SELECT addGeometryColumn($q$ || quote_literal(geom_schema) || $q$,$q$ || quote_literal(edges_table) || $q$, 'geom', 4326, 'LINESTRING', 2) $q$;
	EXECUTE $q$ SELECT addGeometryColumn($q$ || quote_literal(geom_schema) || $q$,$q$ || quote_literal(edges_table) || $q$, 'geom_meters', $q$ || srid_meters || $q$, 'LINESTRING', 2) $q$;

	EXECUTE $q$ CREATE INDEX $q$ || edges_table || $q$_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$ USING GIST (geom)$q$;
	EXECUTE $q$ CREATE INDEX $q$ || edges_table || $q$_meters_idx ON $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$ USING GIST (geom_meters)$q$;

	EXECUTE $q$ INSERT INTO $q$ || quote_ident(geom_schema) || $q$.$q$ || edges_table || $q$(roads_gid, level, label, start_point, end_point, line_length, geom, geom_meters) (SELECT R.gid, R.level, R.label, C1.id,C2.id, Length2D(transform(R.geom,$q$ || srid_meters || $q$)),ST_MakeLine(StartPoint(R.geom), EndPoint(R.geom)), transform(ST_MakeLine(StartPoint(R.geom), EndPoint(R.geom)),$q$ || srid_meters || $q$) FROM $q$ || quote_ident(geom_schema) || $q$.$q$ || roads_table || $q$ R, $q$ || quote_ident(geom_schema) || $q$.$q$ || vertexes_table || $q$ C1, $q$ || quote_ident(geom_schema) || $q$.$q$ || vertexes_table || $q$ C2 WHERE StartPoint(R.geom)~=C1.geom AND EndPoint(R.geom)~=C2.geom)$q$;
END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE STRICT
  COST 100;
