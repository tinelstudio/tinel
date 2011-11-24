/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import java.io.Serializable;
import java.util.List;

import net.tinelstudio.gis.common.dto.StreetDto;

import com.vividsolutions.jts.geom.LineString;

/**
 * Defines a route that is a result of the routing search algorithm.
 * 
 * @author TineL
 */
public interface Route extends Serializable {

  /**
   * @return a list of streets that construct the route (<code>null</code> not
   *         possible)
   */
  List<StreetDto> getStreets();

  /**
   * @return a total cost of the route
   */
  double getCost();

  /**
   * @return a total time taken by the routing search algorithm to compute the
   *         route in milliseconds
   */
  long getTimeTaken();

  /**
   * Merges streets from {@link #getStreets()} to one string of lines.
   * 
   * @return a merged streets or <code>null</code> if no streets
   */
  LineString mergeStreets();
}