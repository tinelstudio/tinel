/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

/**
 * @author TineL
 */
public class CostFunctionFactory {

  /**
   * Creates a cost function from the given {@link Cost}.
   * 
   * @param cost the cost (<code>null</code> not permitted)
   * @return a cost function (<code>null</code> not possible)
   * @throws IllegalArgumentException If the given cost is unknown to this
   *         factory
   */
  public CostFunction createCostFunction(Cost cost)
    throws IllegalArgumentException {
    if (cost instanceof LengthCost) {
      return new LengthCostFunction();

    } else if (cost instanceof LevelLengthCost) {
      LevelLengthCost llc=(LevelLengthCost)cost;
      return new LevelLengthCostFunction(llc.getLevelWeightMap());
    }

    throw new IllegalArgumentException("Unknown cost "+cost);
  }
}
