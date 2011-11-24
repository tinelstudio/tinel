/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.gis.common.domain.Continent;
import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.junit.Test;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author TineL
 */
public class StreetConverterTest extends ConverterSupportTest {

  @Override
  protected StreetConverter createInstance() {
    return new StreetConverter();
  }

  protected StreetConverter createInstance(final LineString deserializedLine) {
    return new StreetConverter() {
      @Override
      public LineString deserializeLineString(LineString line) {
        assertSame(deserializedLine, line);
        return deserializedLine;
      }
    };
  }

  @Test
  public void testConvertStreet() {
    // Init
    Street street=new Street();
    boolean oneWay=true;
    street.setOneWay(oneWay);
    GeoName continent=new GeoName();
    String continentName=Continent.AFRICA;
    continent.setName(continentName);
    continent.setType(Type.CONTINENT);
    GeoName country=new GeoName();
    String countryName="Nowhere";
    country.setName(countryName);
    country.setType(Type.COUNTRY);
    String regionName="Area51";
    GeoName region=new GeoName();
    region.setName(regionName);
    region.setType(Type.REGION);
    String townName="GhostTown";
    GeoName town=new GeoName();
    town.setName(townName);
    town.setType(Type.TOWN);
    String streetName="Alone";
    GeoName name=new GeoName();
    name.setName(streetName);
    name.setType(Type.STREET);
    Set<GeoName> geoNames=new HashSet<GeoName>();
    geoNames.add(continent);
    geoNames.add(country);
    geoNames.add(region);
    geoNames.add(town);
    geoNames.add(name);
    street.setGeoNames(geoNames);
    int lengthMeters=(int)(Math.random()*10000+1);
    street.setLengthMeters(lengthMeters);
    Level level=Level.MEDIUM;
    street.setLevel(level);
    String note="BuckleUp";
    street.setNote(note);
    LineString lineString=createMock(LineString.class);
    street.setLineString(lineString);

    // Run
    StreetConverter rc=createInstance(lineString);
    StreetDto streetDto=rc.convertStreet(street);

    // Verify
    assertEquals(continentName, streetDto.getContinentName());
    assertEquals(countryName, streetDto.getCountryName());
    assertEquals(regionName, streetDto.getRegionName());
    assertEquals(townName, streetDto.getTownName());
    assertEquals(streetName, streetDto.getName());
    assertSame(lineString, streetDto.getLineString());
    assertEquals(lengthMeters, streetDto.getLengthMeters());
    assertSame(level, streetDto.getLevel());
    assertTrue(oneWay==streetDto.isOneWay());
    assertEquals(note, streetDto.getNote());
  }
}
