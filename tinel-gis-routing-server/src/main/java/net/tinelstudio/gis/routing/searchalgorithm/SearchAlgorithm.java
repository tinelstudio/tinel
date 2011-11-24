/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm;

import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

/**
 * Defines the routing search algorithm.
 * 
 * @author TineL
 */
public interface SearchAlgorithm {

  /**
   * Runs the routing search algorithm.
   * 
   * @return a found route or <code>null</code> if none found
   * @throws TimeoutException If the search is taking too much time to find a
   *         route
   */
  Route run() throws TimeoutException;

  /**
   * @return the start node (<code>null</code> not possible)
   */
  StreetNode getStartNode();

  /**
   * @return the goal node (<code>null</code> not possible)
   */
  StreetNode getGoalNode();

  /**
   * @return the cost function g(n) (<code>null</code> not possible)
   */
  CostFunction getCostFunction();

  /**
   * @return the algorithm timeout in milliseconds. If <= 0, no timeout is
   *         defined.
   */
  int getTimeout();
}
