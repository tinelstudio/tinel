/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByNameLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctAddressByTypeLocatorService;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByTownLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctBuildingByTypeLocatorService;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByTypeLocatorService;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByContinentLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByCountryLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByRegionLocator;
import net.tinelstudio.gis.geocoding.locator.bytype.DistinctStreetByTownLocator;
import net.tinelstudio.gis.geocoding.locator.multi.CompositeLocator;
import net.tinelstudio.gis.geocoding.locator.multi.CompositeLocatorService;
import net.tinelstudio.gis.geocoding.locator.multi.FallbackLocator;
import net.tinelstudio.gis.geocoding.locator.multi.FallbackLocatorService;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.GeoName.Type;

/**
 * @author TineL
 */
public class LocatorServiceFactory {

  /**
   * Creates a {@link LocatorService} from the given {@link Locator}.
   * 
   * @param locator the locator (<code>null</code> not permitted)
   * @param findingDao the finding DAO (<code>null</code> not permitted)
   * @return a locator service (<code>null</code> not possible)
   * @throws IllegalArgumentException If the given locator is unknown to this
   *         factory
   */
  public LocatorService createLocatorService(Locator locator,
    FindingDao findingDao) {
    LocatorService ls;
    if (locator instanceof AddressLocator) {
      ls=new AddressLocatorService();

    } else if (locator instanceof DistinctAddressByNameLocator) {
      DistinctAddressByTypeLocatorService dals=new DistinctAddressByTypeLocatorService();
      dals.setType(Type.ADDRESS);
      ls=dals;

    } else if (locator instanceof DistinctAddressByTownLocator) {
      DistinctAddressByTypeLocatorService dals=new DistinctAddressByTypeLocatorService();
      dals.setType(Type.TOWN);
      ls=dals;

    } else if (locator instanceof DistinctAddressByRegionLocator) {
      DistinctAddressByTypeLocatorService dals=new DistinctAddressByTypeLocatorService();
      dals.setType(Type.REGION);
      ls=dals;

    } else if (locator instanceof DistinctAddressByCountryLocator) {
      DistinctAddressByTypeLocatorService dals=new DistinctAddressByTypeLocatorService();
      dals.setType(Type.COUNTRY);
      ls=dals;

    } else if (locator instanceof DistinctAddressByContinentLocator) {
      DistinctAddressByTypeLocatorService dals=new DistinctAddressByTypeLocatorService();
      dals.setType(Type.CONTINENT);
      ls=dals;

    } else if (locator instanceof StreetLocator) {
      ls=new StreetLocatorService();

    } else if (locator instanceof DistinctStreetByTownLocator) {
      DistinctStreetByTypeLocatorService dsls=new DistinctStreetByTypeLocatorService();
      dsls.setType(Type.TOWN);
      ls=dsls;

    } else if (locator instanceof DistinctStreetByRegionLocator) {
      DistinctStreetByTypeLocatorService dsls=new DistinctStreetByTypeLocatorService();
      dsls.setType(Type.REGION);
      ls=dsls;

    } else if (locator instanceof DistinctStreetByCountryLocator) {
      DistinctStreetByTypeLocatorService dsls=new DistinctStreetByTypeLocatorService();
      dsls.setType(Type.COUNTRY);
      ls=dsls;

    } else if (locator instanceof DistinctStreetByContinentLocator) {
      DistinctStreetByTypeLocatorService dsls=new DistinctStreetByTypeLocatorService();
      dsls.setType(Type.CONTINENT);
      ls=dsls;

    } else if (locator instanceof BuildingLocator) {
      ls=new BuildingLocatorService();

    } else if (locator instanceof DistinctBuildingByTownLocator) {
      DistinctBuildingByTypeLocatorService dbls=new DistinctBuildingByTypeLocatorService();
      dbls.setType(Type.TOWN);
      ls=dbls;

    } else if (locator instanceof DistinctBuildingByRegionLocator) {
      DistinctBuildingByTypeLocatorService dbls=new DistinctBuildingByTypeLocatorService();
      dbls.setType(Type.REGION);
      ls=dbls;

    } else if (locator instanceof DistinctBuildingByCountryLocator) {
      DistinctBuildingByTypeLocatorService dbls=new DistinctBuildingByTypeLocatorService();
      dbls.setType(Type.COUNTRY);
      ls=dbls;

    } else if (locator instanceof DistinctBuildingByContinentLocator) {
      DistinctBuildingByTypeLocatorService dbls=new DistinctBuildingByTypeLocatorService();
      dbls.setType(Type.CONTINENT);
      ls=dbls;

    } else if (locator instanceof CompositeLocator) {
      CompositeLocator cl=(CompositeLocator)locator;
      Locator[] locs=cl.getLocators();
      List<LocatorService> lss=new ArrayList<LocatorService>();
      for (Locator l : locs) {
        lss.add(createLocatorService(l, findingDao)); // Recursive
      }
      CompositeLocatorService cls=new CompositeLocatorService();
      cls.setLocators(lss);
      ls=cls;

    } else if (locator instanceof FallbackLocator) {
      FallbackLocator cl=(FallbackLocator)locator;
      Locator[] locs=cl.getLocators();
      List<LocatorService> lss=new ArrayList<LocatorService>();
      for (Locator l : locs) {
        lss.add(createLocatorService(l, findingDao)); // Recursive
      }
      FallbackLocatorService cls=new FallbackLocatorService();
      cls.setLocators(lss);
      ls=cls;

    } else {
      throw new IllegalArgumentException("Unknown locator="+locator);
    }

    ls.setSearchString(locator.getSearchString());
    ls.setMaxResults(locator.getMaxResults());
    ls.setFindingDao(findingDao);
    return ls;
  }
}
