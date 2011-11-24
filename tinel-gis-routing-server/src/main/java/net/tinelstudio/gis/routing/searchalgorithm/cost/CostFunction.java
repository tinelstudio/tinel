/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import net.tinelstudio.gis.model.domain.Street;

/**
 * Defines the behavior of the cost function for the search algorithm.
 * 
 * @author TineL
 * @see Cost
 */
public interface CostFunction extends Cost {

  /**
   * Computes the cost of the given street.
   * 
   * @param street the street (<code>null</code> not permitted)
   * @return a cost of the street
   */
  double computeCost(Street street);
}
