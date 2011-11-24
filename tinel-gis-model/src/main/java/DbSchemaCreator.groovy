/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

import groovy.sql.Sql

/**
 * @author TineL
 */

boolean drop=true // Try to drop tables before create new
String dbUrl="jdbc:postgresql://localhost/tinel-gis"
String dbUser="gis"
String dbPassword="gis"
String dbDriver="org.postgresql.Driver"

SqlBuilder sb=new SqlBuilder()

String query=""
query+=sb.createTable (
    drop:drop,
        name:"address",
        columns:["id bigserial not null", "houseNumber varchar(255) not null", "note varchar(255)"],
        geoColumns:["point":"POINT"],
        pk:"id",
        indices:["houseNumber"]
        );

query+=sb.createTable (
    drop:drop,
        name:"addressrange",
        columns:["id bigserial not null", "fromaddress_id int8", "toaddress_id int8"],
        pk:"id",
        indices:["fromaddress_id", "toaddress_id"]
        );

query+=sb.createTable (
    drop:drop,
        name:"address_geoname",
        columns:["address_id int8 not null", "geonames_id int8 not null"],
        pk:"address_id, geonames_id",
        indices:[]
        );

query+=sb.createTable (
    drop:drop,
        name:"building",
        columns:["id bigserial not null", "note varchar(255)"],
        geoColumns:["polygon":"POLYGON"],
        pk:"id",
        indices:[]
        );

query+=sb.createTable (
    drop:drop,
        name:"building_geoname",
        columns:["building_id int8 not null", "geonames_id int8 not null"],
        pk:"building_id, geonames_id",
        indices:[]
        );

query+=sb.createTable (
    drop:drop,
        name:"geoname",
        columns:["id bigserial not null", "name varchar(255) not null",
        "type varchar(255) not null"],
        pk:"id",
        unique:"name, type",
        indices:["name", "type"]
        );

query+=sb.createTable (
    drop:drop,
        name:"street",
        columns:["id bigserial not null", "lengthmeters int4 not null", "level int4 not null", "oneway bool", "startnode_id int8 not null", "endnode_id int8 not null",  "note varchar(255)"],
        geoColumns:["linestring":"LINESTRING"],
        pk:"id",
        indices:["level", "oneway", "startnode_id", "endnode_id"]
        );

query+=sb.createTable (
    drop:drop,
        name:"streetnode",
        columns:["id bigserial not null"],
        geoColumns:["point":"POINT"],
        pk:"id",
        indices:[]
        );

query+=sb.createTable (
    drop:drop,
        name:"street_addressrange",
        columns:["street_id int8 not null", "leftaddressranges_id int8 not null",
        "rightaddressranges_id int8 not null"],
        pk:"street_id, leftaddressranges_id",
        indices:["rightaddressranges_id"]
        );

query+=sb.createTable (
    drop:drop,
        name:"street_geoname",
        columns:["street_id int8 not null", "geonames_id int8 not null"],
        pk:"street_id, geonames_id",
        indices:[]
        );

query+=sb.createForeignKey(
    table:"addressrange",
        column:"fromaddress_id",
        reference:"address"
        );

query+=sb.createForeignKey(
    table:"addressrange",
        column:"toaddress_id",
        reference:"address"
        );

query+=sb.createForeignKey(
    table:"address_geoname",
        column:"address_id",
        reference:"address"
        );

query+=sb.createForeignKey(
    table:"address_geoname",
        column:"geonames_id",
        reference:"geoname"
        );

query+=sb.createForeignKey(
    table:"building_geoname",
        column:"building_id",
        reference:"building"
        );

query+=sb.createForeignKey(
    table:"building_geoname",
        column:"geonames_id",
        reference:"geoname"
        );

query+=sb.createForeignKey(
    table:"street_addressrange",
        column:"leftaddressranges_id",
        reference:"addressrange"
        );

query+=sb.createForeignKey(
    table:"street_addressrange",
        column:"rightaddressranges_id",
        reference:"addressrange"
        );

query+=sb.createForeignKey(
    table:"street_addressrange",
        column:"street_id",
        reference:"street"
        );

query+=sb.createForeignKey(
    table:"street_geoname",
        column:"street_id",
        reference:"street"
        );

query+=sb.createForeignKey(
    table:"street_geoname",
        column:"geonames_id",
        reference:"geoname"
        );

query+=sb.createForeignKey(
    table:"street",
        column:"startnode_id",
        reference:"streetnode"
        );
query+=sb.createForeignKey(
    table:"street",
        column:"endnode_id",
        reference:"streetnode"
        );

query=sb.transactional(query)

println query

Sql sql=Sql.newInstance(dbUrl, dbUser, dbPassword, dbDriver)
sql.execute(query)