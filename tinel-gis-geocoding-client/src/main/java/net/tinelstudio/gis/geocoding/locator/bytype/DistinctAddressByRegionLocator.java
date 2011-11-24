/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import net.tinelstudio.gis.geocoding.locator.AbstractLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * Specifies which Addresses are to be found. The given search string is
 * compared only against address'es region name. Only one address (usually
 * first) per same region is found.
 * 
 * @author TineL
 * @see Locator
 */
public class DistinctAddressByRegionLocator extends AbstractLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-6353569333390935647L;
}
