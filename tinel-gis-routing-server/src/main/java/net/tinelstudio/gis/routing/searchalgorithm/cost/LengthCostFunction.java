/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import net.tinelstudio.gis.model.domain.Street;

/**
 * The cost function that computes cost by the length of the street.
 * 
 * @author TineL
 * @see LengthCost
 */
public class LengthCostFunction extends LengthCost implements CostFunction {

  @Override
  public double computeCost(Street street) {
    return street.getLengthMeters();
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("LengthCostFunction []");
    return builder.toString();
  }
}
