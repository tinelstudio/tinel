/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Defines the behavior of the heuristic function h(n) for the search algorithm.
 * 
 * @author TineL
 * @see Heuristic
 */
public interface HeuristicFunction extends Heuristic {

  /**
   * Computes the heuristic for the two locations.
   * 
   * @param a the first location (<code>null</code> not permitted)
   * @param b the second location (<code>null</code> not permitted)
   * @return a heuristic
   */
  double computeHeuristic(Coordinate a, Coordinate b);
}
