
package net.tinelstudio.gis.routing.service;

import java.util.concurrent.TimeoutException;

import net.tinelstudio.commons.jts.Deserializer;
import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.ServiceException;
import net.tinelstudio.gis.common.service.AbstractEnablableService;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.StreetNode;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * The default server side implementation of the {@link RoutingService}.
 * 
 * @author TineL
 */
public class DefaultRoutingService extends AbstractEnablableService implements
  RoutingService, InitializingBean {

  protected final Log logger=LogFactory.getLog(getClass());

  private int timeout=60000;

  private int streetNodeMaxDistanceMeters=10000;

  private FindingDao findingDao;

  private AStarSearchAlgorithmFactory aStarSearchAlgorithmFactory;

  private CostFunctionFactory costFunctionFactory;

  private HeuristicFunctionFactory heuristicFunctionFactory;

  private GeometryFactory geometryFactory;

  private Deserializer deserializer;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.aStarSearchAlgorithmFactory);
    Assert.notNull(this.geometryFactory);
    Assert.notNull(this.deserializer);
  }

  protected Route findRouteInternal(RoutingParameters routingParameters)
    throws TimeoutException {

    Coordinate startLocation=routingParameters.getStartLocation();
    Coordinate goalLocation=routingParameters.getGoalLocation();

    if (startLocation.equals(goalLocation)) {
      if (logger.isInfoEnabled()) {
        logger.info("Start location is same as goal location. No route needed");
      }
      return null;
    }

    // Find nodes
    StreetNode startNode=findNearestStreetNode(startLocation);
    StreetNode goalNode=findNearestStreetNode(goalLocation);
    if (startNode==null||goalNode==null) {
      return null;
    }

    Cost cost=routingParameters.getCost();
    CostFunction costFunction=getCostFunctionFactory().createCostFunction(cost);
    Heuristic heuristic=routingParameters.getHeuristic();
    HeuristicFunction heuristicFunction=getHeuristicFunctionFactory()
      .createHeuristicFunction(heuristic);

    // Init algorithm
    AStarSearchAlgorithm aStar=getAStarSearchAlgorithmFactory()
      .createAStarSearchAlgorithm(startNode, goalNode, costFunction,
        heuristicFunction, this.timeout, routingParameters);

    // Run
    return aStar.run();
  }

  @Override
  public Route findRoute(RoutingParameters routingParameters)
    throws NotEnabledException, TimeoutException, ServiceException {
    if (!isEnabled()) throw new NotEnabledException();

    if (logger.isDebugEnabled()) {
      logger.debug("Calling Routing service with "+routingParameters);
    }

    long st=System.currentTimeMillis();

    Route route;
    try {

      route=findRouteInternal(routingParameters);

    } catch (TimeoutException e) {
      throw e;

    } catch (Throwable t) {
      logger.error("", t);
      throw new ServiceException(t);
    }

    if (logger.isDebugEnabled()) {
      long et=System.currentTimeMillis();
      logger.debug("Found route "+route+" in "+(et-st)+" ms");
    }

    return route;
  }

  @Override
  public Point findNearestStreetPoint(Coordinate location)
    throws NotEnabledException, ServiceException {
    if (!isEnabled()) throw new NotEnabledException();

    StreetNode node;
    try {
      node=findNearestStreetNode(location);

    } catch (Throwable t) {
      logger.error("", t);
      throw new ServiceException(t);
    }

    if (node==null) return null;
    Point point=node.getPoint();
    return getDeserializer().deserializePoint(point);
  }

  /**
   * Finds the nearest street node in the DB.
   * 
   * @param location the location (<code>null</code> not permitted)
   * @return a found street node or <code>null</code> if none found
   */
  protected StreetNode findNearestStreetNode(Coordinate location) {
    long st=System.currentTimeMillis();

    Point point=getGeometryFactory().createPoint(location);
    StreetNode node=getFindingDao().findNearestStreetNode(point,
      getStreetNodeMaxDistanceMeters());

    long et=System.currentTimeMillis();

    if (node==null&&logger.isWarnEnabled()) {
      logger.warn("No node found for "+location+" in "+(et-st)+" ms");

    } else if (logger.isInfoEnabled()) {
      logger.info("Node "+node+" found in "+(et-st)+" ms");
    }

    return node;
  }

  @Override
  public int getTimeout() throws NotEnabledException {
    if (!isEnabled()) throw new NotEnabledException();
    return this.timeout;
  }

  @Override
  public long getServerTime() {
    return System.currentTimeMillis();
  }

  public CostFunctionFactory getCostFunctionFactory() {
    if (this.costFunctionFactory==null) {
      this.costFunctionFactory=new CostFunctionFactory();
    }
    return this.costFunctionFactory;
  }

  public void setCostFunctionFactory(CostFunctionFactory costFunctionFactory) {
    this.costFunctionFactory=costFunctionFactory;
  }

  public HeuristicFunctionFactory getHeuristicFunctionFactory() {
    if (this.heuristicFunctionFactory==null) {
      this.heuristicFunctionFactory=new HeuristicFunctionFactory();
    }
    return this.heuristicFunctionFactory;
  }

  public void setHeuristicFunctionFactory(
    HeuristicFunctionFactory heuristicFunctionFactory) {
    this.heuristicFunctionFactory=heuristicFunctionFactory;
  }

  public GeometryFactory getGeometryFactory() {
    return this.geometryFactory;
  }

  public void setGeometryFactory(GeometryFactory geometryFactory) {
    this.geometryFactory=geometryFactory;
  }

  public void setTimeout(int timeout) {
    this.timeout=timeout;
  }

  public int getStreetNodeMaxDistanceMeters() {
    return this.streetNodeMaxDistanceMeters;
  }

  /**
   * @param streetNodeMaxDistanceMeters the search radius for street nodes in
   *        meters (must be > 0)
   */
  public void setStreetNodeMaxDistanceMeters(int streetNodeMaxDistanceMeters) {
    Assert.isTrue(streetNodeMaxDistanceMeters>0,
      "streetNodeMaxDistanceMeters cannot be <= 0");
    this.streetNodeMaxDistanceMeters=streetNodeMaxDistanceMeters;
  }

  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }

  public AStarSearchAlgorithmFactory getAStarSearchAlgorithmFactory() {
    return this.aStarSearchAlgorithmFactory;
  }

  public void setAStarSearchAlgorithmFactory(
    AStarSearchAlgorithmFactory aStarSearchAlgorithmFactory) {
    this.aStarSearchAlgorithmFactory=aStarSearchAlgorithmFactory;
  }

  public Deserializer getDeserializer() {
    return this.deserializer;
  }

  public void setDeserializer(Deserializer deserializer) {
    this.deserializer=deserializer;
  }
}