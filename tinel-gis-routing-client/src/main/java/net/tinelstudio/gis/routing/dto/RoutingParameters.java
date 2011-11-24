/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import java.io.Serializable;

import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Defines the routing parameters needed for the routing search algorithm.
 * 
 * @author TineL
 */
public interface RoutingParameters extends Serializable {

  /**
   * @return the start location of the route
   */
  Coordinate getStartLocation();

  /**
   * @return the goal location of the route
   */
  Coordinate getGoalLocation();

  /**
   * @return the cost function g(n)
   */
  Cost getCost();

  /**
   * @return the heuristic function h(n)
   */
  Heuristic getHeuristic();
}
