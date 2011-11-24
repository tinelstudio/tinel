/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

/**
 * @author TineL
 */
public class HeuristicFunctionFactory {

  /**
   * Creates a heuristic function from the given {@link Heuristic}.
   * 
   * @param heuristic the heuristic (<code>null</code> not permitted)
   * @return a heuristic function (<code>null</code> not possible)
   * @throws IllegalArgumentException If the given heuristic is unknown to this
   *         factory
   */
  public HeuristicFunction createHeuristicFunction(Heuristic heuristic) {
    if (heuristic instanceof GreatCircleDistanceHeuristic) {
      GreatCircleDistanceHeuristic h=(GreatCircleDistanceHeuristic)heuristic;
      return new GreatCircleDistanceHeuristicFunction(h.getWeight());

    } else if (heuristic instanceof NoHeuristic) {
      return new NoHeuristicFunction();
    }

    throw new IllegalArgumentException("Unknown heuristic "+heuristic);
  }
}
