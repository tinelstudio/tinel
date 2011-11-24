/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import net.tinelstudio.gis.geocoding.locator.AddressLocator;
import net.tinelstudio.gis.geocoding.locator.BuildingLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.StreetLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByNameLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByTownLocator;

/**
 * The full composite locator with all already specified {@link Locator}s.
 * 
 * @author TineL
 * @see CompositeLocator
 */
public class FullCompositeLocator extends CompositeLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-7105962703834775731L;

  public FullCompositeLocator() {
    setLocators(
      new AddressLocator(),
      new DistinctAddressByNameLocator(),
      new StreetLocator(),
      new BuildingLocator(),
      // By town
      new DistinctAddressByTownLocator(),
      new DistinctStreetByTownLocator(),
      new DistinctBuildingByTownLocator(),
      // By region
      new DistinctAddressByRegionLocator(),
      new DistinctStreetByRegionLocator(),
      new DistinctBuildingByRegionLocator(),
      // By country
      new DistinctAddressByCountryLocator(),
      new DistinctStreetByCountryLocator(),
      new DistinctBuildingByCountryLocator(),
      // By continent
      new DistinctAddressByContinentLocator(),
      new DistinctStreetByContinentLocator(),
      new DistinctBuildingByContinentLocator());
  }
}
