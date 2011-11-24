/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.gis.common.domain.Continent;
import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.junit.Test;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author TineL
 */
public class BuildingConverterTest extends ConverterSupportTest {

  @Override
  protected BuildingConverter createInstance() {
    return new BuildingConverter();
  }

  protected BuildingConverter createInstance(final Polygon deserializedPolygon) {
    return new BuildingConverter() {
      @Override
      public Polygon deserializePolygon(Polygon polygon) {
        assertSame(deserializedPolygon, polygon);
        return deserializedPolygon;
      }
    };
  }

  @Test
  public void testConvertBuilding() {
    // Init
    Building building=new Building();
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
    String buildName="AlienBar";
    GeoName name=new GeoName();
    name.setName(buildName);
    name.setType(Type.BUILDING);
    Set<GeoName> geoNames=new HashSet<GeoName>();
    geoNames.add(continent);
    geoNames.add(country);
    geoNames.add(region);
    geoNames.add(town);
    geoNames.add(name);
    building.setGeoNames(geoNames);
    Polygon polygon=createMock(Polygon.class);
    building.setPolygon(polygon);
    String note="Note";
    building.setNote(note);

    // Run
    BuildingConverter bc=createInstance(polygon);
    BuildingDto buildingDto=bc.convertBuilding(building);

    // Verify
    assertEquals(continentName, buildingDto.getContinentName());
    assertEquals(countryName, buildingDto.getCountryName());
    assertEquals(regionName, buildingDto.getRegionName());
    assertEquals(townName, buildingDto.getTownName());
    assertEquals(buildName, buildingDto.getName());
    assertEquals(note, buildingDto.getNote());
    assertSame(polygon, buildingDto.getPolygon());
  }
}
