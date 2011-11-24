/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class AbstractLocatorTest {

  protected AbstractLocator createInstance() {
    return new AbstractLocator() {};
  }

  @Test
  public void testSetSearchLocation() {
    Coordinate location=createMock(Coordinate.class);
    AbstractLocator locator=createInstance();
    locator.setSearchLocation(location);
    assertSame(location, locator.getSearchLocation());
  }

  @Test
  public void testSetMaxDistanceMeters() {
    int maxDistanceMeters=(int)(Math.random()*1000-100);
    AbstractLocator locator=createInstance();
    locator.setMaxDistanceMeters(maxDistanceMeters);
    assertEquals(maxDistanceMeters, locator.getMaxDistanceMeters());
  }

  @Test
  public void testSetMaxResults() {
    int maxResults=(int)(Math.random()*1000-100);
    AbstractLocator locator=createInstance();
    locator.setMaxResults(maxResults);
    assertEquals(maxResults, locator.getMaxResults());
  }
}
