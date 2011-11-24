/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import net.tinelstudio.gis.common.dto.StreetDto.Level;

import org.junit.Test;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author TineL
 */
public class DefaultStreetDtoTest extends AbstractPlaceTest {

  @Override
  protected DefaultStreetDto createInstance() {
    return new DefaultStreetDto();
  }

  @Test
  public void testSetName() {
    String name=Double.toHexString(Math.random());
    DefaultStreetDto street=createInstance();
    street.setName(name);
    assertSame(name, street.getName());
  }

  @Test
  public void testSetLineString() {
    LineString line=createMock(LineString.class);
    DefaultStreetDto street=createInstance();
    street.setLineString(line);
    assertSame(line, street.getLineString());
  }

  @Test
  public void testSetLengthMeters() {
    int lengthMeters=(int)(Math.random()*10000+1);
    DefaultStreetDto street=createInstance();
    street.setLengthMeters(lengthMeters);
    assertEquals(lengthMeters, street.getLengthMeters());
  }

  @Test
  public void testSetLevel() {
    DefaultStreetDto street=createInstance();
    Level lengthMeters=Level.MEDIUM;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.MAJOR;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.MINOR;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.UNKNOWN;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
  }

  @Test
  public void testSetOneWay() {
    DefaultStreetDto street=createInstance();
    street.setOneWay(true);
    assertTrue(street.isOneWay());
    street.setOneWay(false);
    assertFalse(street.isOneWay());
  }
}
