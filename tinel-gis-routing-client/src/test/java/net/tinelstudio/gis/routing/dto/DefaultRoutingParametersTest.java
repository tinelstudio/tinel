/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class DefaultRoutingParametersTest {

  protected DefaultRoutingParameters createInstance() {
    return new DefaultRoutingParameters();
  }

  @Test
  public void testSetStartLocation() {
    Coordinate c=createMock(Coordinate.class);
    DefaultRoutingParameters routingParameters=createInstance();
    routingParameters.setStartLocation(c);
    assertSame(c, routingParameters.getStartLocation());
  }

  @Test
  public void testSetGoalLocation() {
    Coordinate c=createMock(Coordinate.class);
    DefaultRoutingParameters routingParameters=createInstance();
    routingParameters.setGoalLocation(c);
    assertSame(c, routingParameters.getGoalLocation());
  }

  @Test
  public void testGetCost() {
    DefaultRoutingParameters routingParameters=createInstance();
    assertSame(DefaultRoutingParameters.cost, routingParameters.getCost());
  }

  @Test
  public void testGetHeuristic() {
    DefaultRoutingParameters routingParameters=createInstance();
    assertSame(DefaultRoutingParameters.heuristic,
      routingParameters.getHeuristic());
  }
}
