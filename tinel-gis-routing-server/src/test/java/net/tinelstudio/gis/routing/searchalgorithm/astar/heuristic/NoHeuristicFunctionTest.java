/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class NoHeuristicFunctionTest extends NoHeuristicTest {

  @Override
  protected NoHeuristicFunction createInstance() {
    return new NoHeuristicFunction();
  }

  @Test
  public void testComputeHeuristic() {
    // Init
    Coordinate a=new Coordinate(14, 46);
    Coordinate b=new Coordinate(15, 47);

    // Run
    NoHeuristicFunction hf=createInstance();
    double heuristic=hf.computeHeuristic(a, b);

    // Verify
    assertEquals(0.0, heuristic, 0.0000);
  }
}
