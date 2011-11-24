/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author TineL
 */
public class HeuristicFunctionFactoryTest {

  @Test(expected=IllegalArgumentException.class)
  public void testCreateHeuristicFunction() {
    Heuristic heuristic=createMock(Heuristic.class);
    HeuristicFunctionFactory factory=new HeuristicFunctionFactory();
    factory.createHeuristicFunction(heuristic);
  }

  @Test
  public void testCreateGreatCircleDistanceHeuristicFunction() {
    // Init
    double weight=Math.random()*100;
    GreatCircleDistanceHeuristic heuristic=createMock(GreatCircleDistanceHeuristic.class);
    expect(heuristic.getWeight()).andReturn(weight);
    replay(heuristic);

    // Run
    HeuristicFunctionFactory factory=new HeuristicFunctionFactory();
    HeuristicFunction heuristicFunction=factory
      .createHeuristicFunction(heuristic);

    // Verify
    verify(heuristic);
    assertTrue(heuristicFunction instanceof GreatCircleDistanceHeuristicFunction);
    assertEquals(weight, heuristicFunction.getWeight(), 0.00001);
  }

  @Test
  public void testCreateNoHeuristicFunction() {
    // Init
    NoHeuristic heuristic=createMock(NoHeuristic.class);

    // Run
    HeuristicFunctionFactory factory=new HeuristicFunctionFactory();
    HeuristicFunction heuristicFunction=factory
      .createHeuristicFunction(heuristic);

    // Verify
    assertTrue(heuristicFunction instanceof NoHeuristicFunction);
  }
}
