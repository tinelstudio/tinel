/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import net.tinelstudio.gis.geocoding.locator.AbstractLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * Specifies which Buildings are to be found. The given search string is
 * compared only against building's continent name. Only one building (usually
 * first) per same continent is found.
 * 
 * @author TineL
 * @see Locator
 */
public class DistinctBuildingByContinentLocator extends AbstractLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-7575723988200328553L;
}
