/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.service;

import java.util.List;

import net.tinelstudio.gis.common.MaxResultsLimitExceededException;
import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.ServiceException;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * Defines the client side of the Geocoding service.
 * 
 * @author TineL
 */
public interface GeocodingService {

  /**
   * Finds geographical places specified by the given locator.
   * 
   * @param locator the locator (<code>null</code> not permitted)
   * @return a list of found places (<code>null</code> not possible)
   * @throws NotEnabledException If the service is not enabled at the moment
   * @throws MaxResultsLimitExceededException If the locator exceeds maximum
   *         results limit set by the service
   * @throws ServiceException If some internal service or server error occurs
   */
  List<? extends Place> find(Locator locator) throws NotEnabledException,
    MaxResultsLimitExceededException, ServiceException;

  /**
   * @return the service maximum results limit. If <= 0, no limit is defined.
   * @throws NotEnabledException If the service is not enabled at the moment
   */
  int getMaxResultsLimit() throws NotEnabledException;

  /**
   * @return the current server time in milliseconds
   */
  long getServerTime();
}
