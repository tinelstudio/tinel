/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author TineL
 */
public class DefaultBuildingDtoTest extends AbstractPlaceTest {

  @Override
  protected DefaultBuildingDto createInstance() {
    return new DefaultBuildingDto();
  }

  @Test
  public void testSetName() {
    String name=Double.toHexString(Math.random());
    DefaultBuildingDto building=createInstance();
    building.setName(name);
    assertSame(name, building.getName());
  }

  @Test
  public void testSetPolygon() {
    Polygon polygon=createMock(Polygon.class);
    DefaultBuildingDto building=createInstance();
    building.setPolygon(polygon);
    assertSame(polygon, building.getPolygon());
  }
}
