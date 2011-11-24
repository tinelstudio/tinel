/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import java.io.Serializable;

/**
 * Defines the heuristic function h(n) for the A* search algorithm.<br>
 * The heuristic function h(n) tells A* an estimate of the minimum cost from any
 * node n to the goal. It’s important to choose a good heuristic function.
 * 
 * @author TineL
 */
public interface Heuristic extends Serializable {

  /**
   * @return the weight applied to the heuristic function
   */
  double getWeight();
}
