/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar;

import net.tinelstudio.gis.routing.searchalgorithm.SearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;

/**
 * Defines the A* routing search algorithm.
 * 
 * @author TineL
 */
public interface AStarSearchAlgorithm extends SearchAlgorithm {

  /**
   * @return the heuristic function h(n) (<code>null</code> not possible)
   */
  HeuristicFunction getHeuristicFunction();
}