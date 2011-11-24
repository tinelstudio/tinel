/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class CustomRoutingParametersTest {

  protected CustomRoutingParameters createInstance() {
    return new CustomRoutingParameters();
  }

  @Test
  public void testSetStartLocation() {
    Coordinate c=createMock(Coordinate.class);
    CustomRoutingParameters routingParameters=createInstance();
    routingParameters.setStartLocation(c);
    assertSame(c, routingParameters.getStartLocation());
  }

  @Test
  public void testSetGoalLocation() {
    Coordinate c=createMock(Coordinate.class);
    CustomRoutingParameters routingParameters=createInstance();
    routingParameters.setGoalLocation(c);
    assertSame(c, routingParameters.getGoalLocation());
  }

  @Test
  public void testSetCost() {
    Cost cost=createMock(Cost.class);
    CustomRoutingParameters routingParameters=createInstance();
    routingParameters.setCost(cost);
    assertSame(cost, routingParameters.getCost());
  }

  @Test
  public void testSetHeuristic() {
    Heuristic heuristic=createMock(Heuristic.class);
    CustomRoutingParameters routingParameters=createInstance();
    routingParameters.setHeuristic(heuristic);
    assertSame(heuristic, routingParameters.getHeuristic());
  }
}
