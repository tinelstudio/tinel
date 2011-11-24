/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author TineL
 */
public class GreatCircleDistanceHeuristicTest extends AbstractHeuristicTest {

  @Override
  protected GreatCircleDistanceHeuristic createInstance() {
    return new GreatCircleDistanceHeuristic();
  }

  protected GreatCircleDistanceHeuristic createInstance(double weight) {
    return new GreatCircleDistanceHeuristic(weight);
  }

  @Test
  public void testConstructor() {
    double weight=Math.random()*100;
    GreatCircleDistanceHeuristic h=createInstance(weight);
    assertEquals(weight, h.getWeight(), 0.0001);
  }
}
