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
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class AddressConverterTest extends ConverterSupportTest {

  @Override
  protected AddressConverter createInstance() {
    return new AddressConverter();
  }

  protected AddressConverter createInstance(final Point deserializedPoint) {
    return new AddressConverter() {
      @Override
      public Point deserializePoint(Point point) {
        assertSame(deserializedPoint, point);
        return deserializedPoint;
      }
    };
  }

  @Test
  public void testConvertAddress() {
    // Init
    Address address=new Address();
    String houseNumber="103a";
    address.setHouseNumber(houseNumber);
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
    String addressName="Alone";
    GeoName name=new GeoName();
    name.setName(addressName);
    name.setType(Type.ADDRESS);
    Set<GeoName> geoNames=new HashSet<GeoName>();
    geoNames.add(continent);
    geoNames.add(country);
    geoNames.add(region);
    geoNames.add(town);
    geoNames.add(name);
    address.setGeoNames(geoNames);
    Point point=createMock(Point.class);
    address.setPoint(point);
    String note="Note";
    address.setNote(note);

    // Run
    AddressConverter ac=createInstance(point);
    AddressDto addressDto=ac.convertAddress(address);

    // Verify
    assertEquals(continentName, addressDto.getContinentName());
    assertEquals(countryName, addressDto.getCountryName());
    assertEquals(regionName, addressDto.getRegionName());
    assertEquals(townName, addressDto.getTownName());
    assertEquals(addressName, addressDto.getName());
    assertEquals(houseNumber, addressDto.getHouseNumber());
    assertEquals(note, addressDto.getNote());
    assertSame(point, addressDto.getPoint());
  }
}
