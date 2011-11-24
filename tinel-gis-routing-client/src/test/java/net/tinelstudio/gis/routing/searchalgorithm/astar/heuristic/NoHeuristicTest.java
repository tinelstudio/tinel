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
public class NoHeuristicTest extends AbstractHeuristicTest {

  @Override
  protected NoHeuristic createInstance() {
    return new NoHeuristic();
  }

  @Test
  public void testConstructor() {
    NoHeuristic h=createInstance();
    assertEquals(0.0, h.getWeight(), 0.0);
  }
}
