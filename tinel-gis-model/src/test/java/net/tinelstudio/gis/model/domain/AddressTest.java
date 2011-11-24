/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class AddressTest extends NotedEntityTest {

  @Override
  protected Address createInstance() {
    return new Address();
  }

  @Test
  public void testSetPoint() {
    Point point=createMock(Point.class);
    Address address=createInstance();
    address.setPoint(point);
    assertSame(point, address.getPoint());
  }

  @Test
  public void testSetGeoNames() {
    Set<GeoName> geoNames=new HashSet<GeoName>();
    Address address=createInstance();
    address.setGeoNames(geoNames);
    assertSame(geoNames, address.getGeoNames());
  }

  @Test
  public void testSetHouseNumber() {
    String houseNumber="103a";
    Address address=createInstance();
    address.setHouseNumber(houseNumber);
    assertSame(houseNumber, address.getHouseNumber());
  }
}
