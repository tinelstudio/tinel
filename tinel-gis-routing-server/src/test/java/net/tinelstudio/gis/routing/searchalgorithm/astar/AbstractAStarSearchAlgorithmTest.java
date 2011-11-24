/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.searchalgorithm.AbstractSearchAlgorithmTest;
import net.tinelstudio.gis.routing.searchalgorithm.astar.AbstractAStarSearchAlgorithm.StreetNodeDecorator;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class AbstractAStarSearchAlgorithmTest extends
  AbstractSearchAlgorithmTest {

  @Override
  protected AbstractAStarSearchAlgorithm createInstance() {
    return new AbstractAStarSearchAlgorithm() {
      @Override
      public Route run() throws TimeoutException {
        throw new UnsupportedOperationException();
      }

      @Override
      protected Collection<Street> findConnectedStreets(StreetNode streetNode) {
        throw new UnsupportedOperationException();
      }
    };
  }

  protected AbstractAStarSearchAlgorithm createInstance(final int timeout,
    final Point startPoint, final StreetNode expectedStartStreetNode,
    final StreetNode expectedGoalStreetNode) {
    return new AbstractAStarSearchAlgorithm() {

      private boolean start=true;

      @Override
      protected Collection<Street> findConnectedStreets(StreetNode streetNode) {
        if (start) {
          assertEquals(expectedStartStreetNode, streetNode);
          start=false;

        } else {
          assertEquals(expectedGoalStreetNode, streetNode);
        }
        try {
          // Add 500 ms to eliminate System.currentMillis() small inaccuracy
          Thread.sleep(500+timeout);

        } catch (InterruptedException e) {
          fail();
        }
        return Collections.emptyList();
      }

      @Override
      protected double h(Point pos) {
        assertSame(startPoint, pos);
        return 15;
      }
    };
  }

  private void testRun(final int timeout) throws TimeoutException {
    // Init
    final Point startPoint=createMock(Point.class);

    StreetNode startNode=createMock(StreetNode.class);
    expect(startNode.getId()).andReturn(1L).anyTimes();
    expect(startNode.getPoint()).andReturn(startPoint).anyTimes();

    Point goalPoint=createMock(Point.class);

    StreetNode goalNode=createMock(StreetNode.class);
    expect(goalNode.getId()).andReturn(10L).anyTimes();
    expect(goalNode.getPoint()).andReturn(goalPoint).anyTimes();

    CostFunction costFunction=createMock(CostFunction.class);

    HeuristicFunction heuristicFunction=createMock(HeuristicFunction.class);

    replay(startNode, goalNode);

    // Run
    AbstractAStarSearchAlgorithm aStar=createInstance(timeout, startPoint,
      startNode, goalNode);
    aStar.setStartNode(startNode);
    aStar.setGoalNode(goalNode);
    aStar.setCostFunction(costFunction);
    aStar.setHeuristicFunction(heuristicFunction);
    aStar.setTimeout(timeout);
    Route route=aStar.run();

    // Verify
    verify(startNode, goalNode);
    assertNull(route);
  }

  @Test
  public void testRun() throws TimeoutException {
    testRun(0); // Unlimited timeout
  }

  @Test(expected=TimeoutException.class)
  public void testRunTimeout() throws TimeoutException {
    testRun(1); // Timeout only 1 ms
  }

  @Test
  public void testReconstructPath() {
    // Init
    Long node1Id=10L;
    StreetNodeDecorator node1=createMock(StreetNodeDecorator.class);
    expect(node1.getId()).andReturn(node1Id).anyTimes();

    Long node2Id=11L;
    StreetNodeDecorator node2=createMock(StreetNodeDecorator.class);
    expect(node2.getId()).andReturn(node2Id).anyTimes();

    Long node3Id=12L;
    StreetNodeDecorator node3=createMock(StreetNodeDecorator.class);
    expect(node3.getId()).andReturn(node3Id).anyTimes();

    Long goalNodeId=100L;
    StreetNodeDecorator goalNode=createMock(StreetNodeDecorator.class);
    expect(goalNode.getId()).andReturn(goalNodeId).anyTimes();

    Map<Long, StreetNodeDecorator> nodeMap=new HashMap<Long, StreetNodeDecorator>();
    nodeMap.put(goalNodeId, node1);
    nodeMap.put(node1Id, node2);
    nodeMap.put(node2Id, node3);
    nodeMap.put(node3Id, null);

    Street street1=createMock(Street.class);
    Street street2=createMock(Street.class);
    Street street3=createMock(Street.class);
    Street goalStreet=createMock(Street.class);

    Map<Long, Street> streetMap=new HashMap<Long, Street>();
    streetMap.put(goalNodeId, goalStreet);
    streetMap.put(node1Id, street1);
    streetMap.put(node2Id, street2);
    streetMap.put(node3Id, street3);

    replay(node1, node2, node3, goalNode);

    // Run
    AbstractAStarSearchAlgorithm aStar=createInstance();
    List<Street> streets=aStar.reconstructPath(nodeMap, streetMap, goalNode);

    // Verify
    verify(node1, node2, node3, goalNode);
    assertSame(street3, streets.get(0));
    assertSame(street2, streets.get(1));
    assertSame(street1, streets.get(2));
    assertSame(goalStreet, streets.get(3));
  }

  @Test
  public void testSetHeuristicFunction() {
    HeuristicFunction heuristicFunction=createMock(HeuristicFunction.class);
    AbstractAStarSearchAlgorithm aStar=createInstance();
    aStar.setHeuristicFunction(heuristicFunction);
    assertEquals(heuristicFunction, aStar.getHeuristicFunction());
  }

  @Test
  public void testH() {
    // Init
    Coordinate location=createMock(Coordinate.class);
    Coordinate goalLocation=createMock(Coordinate.class);

    Point point=createMock(Point.class);
    expect(point.getCoordinate()).andReturn(location);

    Point goalPoint=createMock(Point.class);
    expect(goalPoint.getCoordinate()).andReturn(goalLocation);

    StreetNode goalNode=createMock(StreetNode.class);
    expect(goalNode.getPoint()).andReturn(goalPoint);

    double h=Math.random()*100;

    HeuristicFunction heuristicFunction=createMock(HeuristicFunction.class);
    expect(heuristicFunction.computeHeuristic(location, goalLocation))
      .andReturn(h);

    replay(point, goalPoint, goalNode, heuristicFunction);

    // Run
    AbstractAStarSearchAlgorithm aStar=createInstance();
    aStar.setHeuristicFunction(heuristicFunction);
    aStar.setGoalNode(goalNode);
    double heuristic=aStar.h(point);

    // Verify
    verify(point, goalPoint, goalNode, heuristicFunction);
    assertEquals(h, heuristic, 0.0001);
  }
}
