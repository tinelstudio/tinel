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

import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * The transactional server side implementation of the {@link RoutingService}.
 * 
 * @author TineL
 */
public class TransactionalRoutingService extends DefaultRoutingService {

  @Transactional(readOnly=true)
  @Override
  public Route findRoute(RoutingParameters routingParameters)
    throws NotEnabledException, TimeoutException, ServiceException {
    return super.findRoute(routingParameters);
  }

  @Transactional(readOnly=true)
  @Override
  public Point findNearestStreetPoint(Coordinate location)
    throws NotEnabledException, ServiceException {
    return super.findNearestStreetPoint(location);
  }
}
