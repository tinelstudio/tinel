/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.geocoding.locator.LocatorService;

import org.springframework.util.StringUtils;

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
  public List<Place> find() {
    List<Place> places=new ArrayList<Place>();

    for (LocatorService ls : getLocators()) {
      ls.setFindingDao(getFindingDao());

      // Adapt max results to not exceed overall number
      int resutltsLeft=getMaxResults()-places.size();
      if (ls.getMaxResults()<=0||ls.getMaxResults()>resutltsLeft) {
        ls.setMaxResults(resutltsLeft);
      }

      // Leave custom locator service search string if it was set
      if (!StringUtils.hasText(ls.getSearchString())) {
        ls.setSearchString(getSearchString());
      }

      places.addAll(ls.find());

      if (places.size()>=getMaxResults()) {
        // Max results richen; stop
        break;
      }
    }
    return places;
  }
}
