/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.service;

import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.ServiceException;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.dto.RoutingParameters;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Defines the client side of the Routing service.
 * 
 * @author TineL
 */
public interface RoutingService {

  /**
   * Finds the nearest point of the given location that lies on a known street.
   * 
   * @param location the location (<code>null</code> not permitted)
   * @return the nearest point of the location or <code>null</code> if not found
   * @throws NotEnabledException If the service is not enabled at the moment
   * @throws ServiceException If some internal service or server error occurs
   */
  Point findNearestStreetPoint(Coordinate location) throws NotEnabledException,
    ServiceException;

  /**
   * Finds a route.
   * 
   * @param routingParameters the routing parameters passed to routing search
   *        algorithm (<code>null</code> not permitted)
   * @return a found route or <code>null</code> if none found
   * @throws NotEnabledException If the service is not enabled at the moment
   * @throws TimeoutException If the routing search is taking too much time to
   *         find a route
   * @throws ServiceException If some internal service or server error occurs
   */
  Route findRoute(RoutingParameters routingParameters)
    throws NotEnabledException, TimeoutException, ServiceException;

  /**
   * @return the service timeout of the routing search in milliseconds. If <= 0,
   *         no timeout is defined.
   * @throws NotEnabledException If the service is not enabled at the moment
   */
  int getTimeout() throws NotEnabledException;

  /**
   * @return the current server time in milliseconds
   */
  long getServerTime();
}
