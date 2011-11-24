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

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author TineL
 */
public class BuildingTest extends NotedEntityTest {

  @Override
  protected Building createInstance() {
    return new Building();
  }

  @Test
  public void testSetPolygon() {
    Polygon polygon=createMock(Polygon.class);
    Building building=createInstance();
    building.setPolygon(polygon);
    assertSame(polygon, building.getPolygon());
  }

  @Test
  public void testSetGeoNames() {
    Set<GeoName> geoNames=new HashSet<GeoName>();
    Building building=createInstance();
    building.setGeoNames(geoNames);
    assertSame(geoNames, building.getGeoNames());
  }
}
