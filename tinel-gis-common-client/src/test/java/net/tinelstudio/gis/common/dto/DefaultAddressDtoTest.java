/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class DefaultAddressDtoTest extends AbstractPlaceTest {

  @Override
  protected DefaultAddressDto createInstance() {
    return new DefaultAddressDto();
  }

  @Test
  public void testSetStreetName() {
    String name=Double.toHexString(Math.random());
    DefaultAddressDto address=createInstance();
    address.setName(name);
    assertSame(name, address.getName());
  }

  @Test
  public void testSetHouseNumber() {
    String houseNumber=Double.toHexString(Math.random());
    DefaultAddressDto address=createInstance();
    address.setHouseNumber(houseNumber);
    assertSame(houseNumber, address.getHouseNumber());
  }

  @Test
  public void testSetPoint() {
    Point point=createMock(Point.class);
    DefaultAddressDto address=createInstance();
    address.setPoint(point);
    assertSame(point, address.getPoint());
  }
}
