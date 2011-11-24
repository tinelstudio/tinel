/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator.multi;

import java.util.Collections;
import java.util.List;

import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.reversegeocoding.locator.LocatorService;

/**
 * This locator service is a special composite of multiple
 * {@link LocatorService}s. During the search, the results of only one locator
 * service of all here specified are returned - which first finds something.<br>
 * If the first locator service does not find anything, the search is
 * fall-backed to next locator service and so on.
 * 
 * @author TineL
 */
public class FallbackLocatorService extends AbstractMultiLocatorService {

  @Override
  public List<? extends Place> findNearest() {
    for (LocatorService ls : getLocators()) {
      ls.setFindingDao(getFindingDao());

      // Leave custom locator service maxResults if it was set
      if (ls.getMaxResults()<=0) {
        ls.setMaxResults(getMaxResults());
      }

      // Leave custom locator service maxDistanceMeters if it was set
      if (ls.getMaxDistanceMeters()<=0) {
        ls.setMaxDistanceMeters(getMaxDistanceMeters());
      }

      // Leave custom locator service search point it it was set
      if (ls.getSearchLocation()==null) {
        ls.setSearchLocation(getSearchLocation());
      }

      List<? extends Place> places=ls.findNearest();
      if (!places.isEmpty()) {
        // There are results; return them and finish
        return places;
      }
      // No results, fall-back to next locator service
    }
    // No results at all; every locator service returned no results
    return Collections.emptyList();
  }
}
