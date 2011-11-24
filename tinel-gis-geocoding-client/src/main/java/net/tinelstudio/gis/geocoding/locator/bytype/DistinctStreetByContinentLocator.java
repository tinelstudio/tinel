/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import net.tinelstudio.gis.geocoding.locator.AbstractLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * Specifies which Streets are to be found. The given search string is compared
 * only against street's continent name. Only one street (usually first) per
 * same continent is found.
 * 
 * @author TineL
 * @see Locator
 */
public class DistinctStreetByContinentLocator extends AbstractLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-4257891903193727187L;
}
