/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.util.List;

import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.model.dao.FindingDao;

/**
 * Defines the service (server side) of the {@link Locator}.
 * 
 * @author TineL
 * @see Locator
 */
public interface LocatorService extends Locator {

  /**
   * Finds nearby geographical places specified by this locator.
   * 
   * @return a list of found places (<code>null</code> not possible)
   */
  List<? extends Place> findNearest();

  FindingDao getFindingDao();

  void setFindingDao(FindingDao findingDao);
}
