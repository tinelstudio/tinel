/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByNameLocator;

/**
 * Specifies which Addresses are to be found. The given search string is
 * compared against address'es name AND house number.<br>
 * <br>
 * <b>Warning:</b> AddressLocator will work only if provided search string
 * contains house number(s) - some digits. To locate addresses without provided
 * house number(s), use {@link DistinctAddressByNameLocator} instead.
 * 
 * @author TineL
 * @see Locator
 */
public class AddressLocator extends AbstractLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-6184370621832259526L;
}
