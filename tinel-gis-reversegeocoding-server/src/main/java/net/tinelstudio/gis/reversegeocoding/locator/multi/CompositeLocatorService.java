/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator.multi;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.reversegeocoding.locator.LocatorService;

/**
 * This locator service is a composite of multiple {@link LocatorService}s.
 * During the search, all locator services here specified are used, one by one.
 * 
 * @author TineL
 * @see MultiLocatorService
 * @see CompositeLocator
 */
public class CompositeLocatorService extends AbstractMultiLocatorService {

  @Override
  public List<? extends Place> findNearest() {
    List<Place> places=new ArrayList<Place>();

    for (LocatorService ls : getLocators()) {
      ls.setFindingDao(getFindingDao());

      // Adapt max results to not exceed overall number
      int resutltsLeft=getMaxResults()-places.size();
      if (ls.getMaxResults()<=0||ls.getMaxResults()>resutltsLeft) {
        ls.setMaxResults(resutltsLeft);
      }

      // Leave custom locator service maxDistanceMeters if it was set
      if (ls.getMaxDistanceMeters()<=0) {
        ls.setMaxDistanceMeters(getMaxDistanceMeters());
      }

      // Leave custom locator service search point if it was set
      if (ls.getSearchLocation()==null) {
        ls.setSearchLocation(getSearchLocation());
      }

      places.addAll(ls.findNearest());

      if (places.size()>=getMaxResults()) {
        // Max results richen; stop
        break;
      }
    }
    return places;
  }
}
