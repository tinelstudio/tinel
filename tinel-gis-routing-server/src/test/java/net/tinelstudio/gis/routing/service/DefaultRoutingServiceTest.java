/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.service.AbstractEnablableServiceTest;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.DefaultRoute;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.dto.RoutingParameters;
import net.tinelstudio.gis.routing.searchalgorithm.astar.AStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.factory.AStarSearchAlgorithmFactory;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunctionFactory;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunctionFactory;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class DefaultRoutingServiceTest extends AbstractEnablableServiceTest {

  @Override
  protected DefaultRoutingService createInstance() {
    return new DefaultRoutingService();
  }

  protected DefaultRoutingService createInstance(
    final RoutingParameters expectedRoutingParameters, final Route returnRoute) {
    return new DefaultRoutingService() {
      @Override
      protected Route findRouteInternal(RoutingParameters routingParameters)
        throws TimeoutException {
        assertSame(expectedRoutingParameters, routingParameters);
        return returnRoute;
      }
    };
  }

  protected DefaultRoutingService createInstance(
    final Coordinate expectedLocation1, final Coordinate expectedLocation2,
    final StreetNode returnNode) {
    return new DefaultRoutingService() {
      @Override
      protected StreetNode findNearestStreetNode(Coordinate c1) {
        assertTrue(expectedLocation1==c1||expectedLocation2==c1);
        return returnNode;
      }
    };
  }

  @Test
  public void testSetCostFunctionFactory() {
    CostFunctionFactory factory=createMock(CostFunctionFactory.class);
    DefaultRoutingService service=createInstance();
    service.setCostFunctionFactory(factory);
    assertSame(factory, service.getCostFunctionFactory());
  }

  @Test
  public void testSetHeuristicFunctionFactory() {
    HeuristicFunctionFactory factory=createMock(HeuristicFunctionFactory.class);
    DefaultRoutingService service=createInstance();
    service.setHeuristicFunctionFactory(factory);
    assertSame(factory, service.getHeuristicFunctionFactory());
  }

  @Test
  public void testSetFindingDao() {
    FindingDao findingDao=createMock(FindingDao.class);
    DefaultRoutingService service=createInstance();
    service.setFindingDao(findingDao);
    assertSame(findingDao, service.getFindingDao());
  }

  @Test
  public void testSetAStarSearchAlgorithmFactory() {
    AStarSearchAlgorithmFactory factory=createMock(AStarSearchAlgorithmFactory.class);
    DefaultRoutingService service=createInstance();
    service.setAStarSearchAlgorithmFactory(factory);
    assertSame(factory, service.getAStarSearchAlgorithmFactory());
  }

  @Test
  public void testSetGeometryFactory() {
    GeometryFactory gf=createMock(GeometryFactory.class);
    DefaultRoutingService service=createInstance();
    service.setGeometryFactory(gf);
    assertSame(gf, service.getGeometryFactory());
  }

  @Test(expected=NotEnabledException.class)
  public void testGetTimeoutDisabled() throws Exception {
    DefaultRoutingService service=createInstance();
    service.setEnabled(false);
    service.getTimeout();
  }

  @Test
  public void testSetTimeout() throws Exception {
    int timeout=(int)(Math.random()*1000-100);
    DefaultRoutingService service=createInstance();
    service.setTimeout(timeout);
    assertEquals(timeout, service.getTimeout());
  }

  @Test
  public void testSetStreetNodeMaxDistanceMeters() {
    int streetNodeMaxDistanceMeters=(int)(Math.random()*1000+1);
    DefaultRoutingService service=createInstance();
    service.setStreetNodeMaxDistanceMeters(streetNodeMaxDistanceMeters);
    assertEquals(streetNodeMaxDistanceMeters,
      service.getStreetNodeMaxDistanceMeters());
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSetStreetNodeMaxDistanceMetersNegative() {
    int streetNodeMaxDistanceMeters=(int)(Math.random()*-1000);
    DefaultRoutingService service=createInstance();
    service.setStreetNodeMaxDistanceMeters(streetNodeMaxDistanceMeters);
    assertEquals(streetNodeMaxDistanceMeters,
      service.getStreetNodeMaxDistanceMeters());
  }

  @Test
  public void testGetServerTime() {
    double time=System.currentTimeMillis();
    DefaultRoutingService service=createInstance();
    assertEquals(time, service.getServerTime(), 1000); // 1000 ms delta
  }

  @Test(expected=NotEnabledException.class)
  public void testFindRouteDisabled() throws Exception {
    DefaultRoutingService service=createInstance();
    service.setEnabled(false);
    service.findRoute(null);
  }

  @Test
  public void testFindRoute() throws Exception {
    // Init
    Route route=createMock(DefaultRoute.class);
    RoutingParameters routingParameters=createMock(RoutingParameters.class);

    // Run
    DefaultRoutingService service=createInstance(routingParameters, route);
    Route actualRoute=service.findRoute(routingParameters);

    // Verify
    assertSame(route, actualRoute);
  }

  @Test
  public void testFindRouteInternal() throws Exception {
    // Init
    Coordinate location1=createMock(Coordinate.class);
    Coordinate location2=createMock(Coordinate.class);
    StreetNode node=createMock(StreetNode.class);
    Cost cost=createMock(Cost.class);
    Heuristic heuristic=createMock(Heuristic.class);

    RoutingParameters routingParameters=createMock(RoutingParameters.class);
    expect(routingParameters.getStartLocation()).andReturn(location1);
    expect(routingParameters.getGoalLocation()).andReturn(location2);
    expect(routingParameters.getCost()).andReturn(cost);
    expect(routingParameters.getHeuristic()).andReturn(heuristic);

    CostFunction costFunction=createMock(CostFunction.class);
    CostFunctionFactory costFunctionFactory=createMock(CostFunctionFactory.class);
    expect(costFunctionFactory.createCostFunction(cost))
      .andReturn(costFunction);

    HeuristicFunction heuristicFunction=createMock(HeuristicFunction.class);
    HeuristicFunctionFactory heuristicFunctionFactory=createMock(HeuristicFunctionFactory.class);
    expect(heuristicFunctionFactory.createHeuristicFunction(heuristic))
      .andReturn(heuristicFunction);

    Route route=createMock(Route.class);
    int timeout=(int)(Math.random()*100000+1);

    AStarSearchAlgorithm searchAlg=createMock(AStarSearchAlgorithm.class);
    expect(searchAlg.run()).andReturn(route);

    AStarSearchAlgorithmFactory factory=createMock(AStarSearchAlgorithmFactory.class);
    expect(
      factory.createAStarSearchAlgorithm(node, node, costFunction,
        heuristicFunction, timeout, routingParameters)).andReturn(searchAlg);

    replay(routingParameters, costFunctionFactory, heuristicFunctionFactory,
      factory, searchAlg);

    // Run
    DefaultRoutingService service=createInstance(location1, location2, node);
    service.setCostFunctionFactory(costFunctionFactory);
    service.setHeuristicFunctionFactory(heuristicFunctionFactory);
    service.setAStarSearchAlgorithmFactory(factory);
    service.setTimeout(timeout);
    Route actualRoute=service.findRouteInternal(routingParameters);

    // Verify
    verify(routingParameters, costFunctionFactory, heuristicFunctionFactory,
      factory, searchAlg);
    assertSame(route, actualRoute);
  }

  @Test
  public void testFindNearestStreetNode() {
    // Init
    StreetNode node=createMock(StreetNode.class);
    Coordinate c=new Coordinate(15, 46);
    GeometryFactory gf=new GeometryFactory();
    Point point=gf.createPoint(c);

    GeometryFactory gfMock=createMock(GeometryFactory.class);
    expect(gfMock.createPoint(c)).andReturn(point);

    FindingDao findingDao=createMock(FindingDao.class);
    expect(findingDao.findNearestStreetNode(point, 10000)).andReturn(node);

    replay(gfMock, findingDao);

    // Run
    DefaultRoutingService service=createInstance();
    service.setGeometryFactory(gfMock);
    service.setFindingDao(findingDao);
    StreetNode actualNode=service.findNearestStreetNode(c);

    // Verify
    verify(gfMock, findingDao);
    assertSame(node, actualNode);
  }
}
