/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

import static org.junit.Assert.assertEquals;
import net.tinelstudio.commons.DistanceOnEarth;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class GreatCircleDistanceHeuristicFunctionTest extends
  GreatCircleDistanceHeuristicTest {

  @Override
  protected GreatCircleDistanceHeuristicFunction createInstance() {
    return new GreatCircleDistanceHeuristicFunction();
  }

  @Override
  protected GreatCircleDistanceHeuristicFunction createInstance(double weight) {
    return new GreatCircleDistanceHeuristicFunction(weight);
  }

  @Test
  public void testComputeHeuristic() {
    // Init
    Coordinate a=new Coordinate(14, 46);
    Coordinate b=new Coordinate(15, 47);

    double distance=DistanceOnEarth.distance(a.y, a.x, b.y, b.x);

    double weight=Math.random()*100;

    // Run
    GreatCircleDistanceHeuristicFunction hf=createInstance();
    hf.setWeight(weight);
    double heuristic=hf.computeHeuristic(a, b);

    // Verify
    assertEquals(distance*weight, heuristic, 0.0001);
  }
}
