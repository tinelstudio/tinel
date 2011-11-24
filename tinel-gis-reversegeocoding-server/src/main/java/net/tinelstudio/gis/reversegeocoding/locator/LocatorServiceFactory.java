/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.reversegeocoding.locator.multi.CompositeLocator;
import net.tinelstudio.gis.reversegeocoding.locator.multi.CompositeLocatorService;
import net.tinelstudio.gis.reversegeocoding.locator.multi.FallbackLocator;
import net.tinelstudio.gis.reversegeocoding.locator.multi.FallbackLocatorService;

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

    } else if (locator instanceof StreetLocator) {
      ls=new StreetLocatorService();

    } else if (locator instanceof BuildingLocator) {
      ls=new BuildingLocatorService();

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

    ls.setSearchLocation(locator.getSearchLocation());
    ls.setMaxDistanceMeters(locator.getMaxDistanceMeters());
    ls.setMaxResults(locator.getMaxResults());
    ls.setFindingDao(findingDao);
    return ls;
  }
}
