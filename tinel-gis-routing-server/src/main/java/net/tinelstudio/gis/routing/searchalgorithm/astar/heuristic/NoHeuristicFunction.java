/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The heuristic function without any effect on A* search algorithm.
 * 
 * @author TineL
 * @see NoHeuristic
 */
public class NoHeuristicFunction extends NoHeuristic implements
  HeuristicFunction {

  /**
   * Constructs a heuristic function with weight = 0.0.
   */
  public NoHeuristicFunction() {}

  @Override
  public double computeHeuristic(Coordinate a, Coordinate b) {
    return 0.0;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("NoHeuristicFunction []");
    return builder.toString();
  }
}
