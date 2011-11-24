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
public class AbstractHeuristicTest {

  protected AbstractHeuristic createInstance() {
    return new AbstractHeuristic() {};
  }

  @Test
  public void testSetWeight() {
    double weight=Math.random()*100;
    AbstractHeuristic h=createInstance();
    h.setWeight(weight);
    assertEquals(weight, h.getWeight(), 0.0001);
  }
}
