/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import net.tinelstudio.commons.DistanceOnEarth;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The heuristic function that computes heuristic by the great-circle distance
 * between two locations multiplied with weight. Using {@link DistanceOnEarth}.
 * 
 * @author TineL
 * @see GreatCircleDistanceHeuristic
 */
public class GreatCircleDistanceHeuristicFunction extends
  GreatCircleDistanceHeuristic implements HeuristicFunction {

  /**
   * Constructs a heuristic function with weight = 1.0.
   */
  public GreatCircleDistanceHeuristicFunction() {}

  /**
   * Constructs a heuristic with the specified weight.
   * 
   * @param weight the heuristic weight
   * @see #getWeight()
   */
  public GreatCircleDistanceHeuristicFunction(double weight) {
    super(weight);
  }

  @Override
  public double computeHeuristic(Coordinate a, Coordinate b) {
    return DistanceOnEarth.distance(a.y, a.x, b.y, b.x)*getWeight();
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("GreatCircleDistanceHeuristicFunction [getWeight()=")
      .append(this.getWeight()).append("]");
    return builder.toString();
  }
}
