/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author TineL
 */
public class AbstractLocatorTest {

  protected AbstractLocator createInstance() {
    return new AbstractLocator() {};
  }

  @Test
  public void testSetSearchString() {
    String ss="SearchString";
    AbstractLocator locator=createInstance();
    locator.setSearchString(ss);
    assertSame(ss, locator.getSearchString());
  }

  @Test
  public void testSetMaxResults() {
    int maxResults=(int)(Math.random()*1000+1);
    AbstractLocator locator=createInstance();
    locator.setMaxResults(maxResults);
    assertEquals(maxResults, locator.getMaxResults());
  }
}
