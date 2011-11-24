/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.factory;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.RoutingParameters;
import net.tinelstudio.gis.routing.searchalgorithm.astar.AStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.DefaultAStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

import org.junit.Test;

/**
 * @author TineL
 */
public class DefaultAStarSearchAlgorithmFactoryTest {

  protected DefaultAStarSearchAlgorithmFactory createInstance() {
    return new DefaultAStarSearchAlgorithmFactory();
  }

  protected DefaultAStarSearchAlgorithmFactory createInstance(
    final DefaultAStarSearchAlgorithm aStar) {
    DefaultAStarSearchAlgorithmFactory factory=new DefaultAStarSearchAlgorithmFactory() {
      @Override
      protected DefaultAStarSearchAlgorithm createAStarSearchAlgorithm() {
        return aStar;
      }
    };
    return factory;
  }

  @Test
  public void testSetFindingDao() {
    FindingDao findingDao=createMock(FindingDao.class);
    DefaultAStarSearchAlgorithmFactory factory=createInstance();
    factory.setFindingDao(findingDao);
    assertEquals(findingDao, factory.getFindingDao());
  }

  @Test
  public void testCreateSearchAlgorithmInternal() {
    DefaultAStarSearchAlgorithmFactory factory=createInstance();
    DefaultAStarSearchAlgorithm aStar=factory.createAStarSearchAlgorithm();
    assertNotNull(aStar);
  }

  @Test
  public void testCreateSearchAlgorithm() {
    // Init
    FindingDao findingDao=createMock(FindingDao.class);
    StreetNode startNode=createMock(StreetNode.class);
    StreetNode goalNode=createMock(StreetNode.class);
    CostFunction costFunction=createMock(CostFunction.class);
    HeuristicFunction heuristicFunction=createMock(HeuristicFunction.class);
    int timeout=(int)(Math.random()*1000);
    RoutingParameters routingParameters=createMock(RoutingParameters.class);

    DefaultAStarSearchAlgorithm aStar=createMock(DefaultAStarSearchAlgorithm.class);
    aStar.setFindingDao(findingDao);
    aStar.setStartNode(startNode);
    aStar.setGoalNode(goalNode);
    aStar.setCostFunction(costFunction);
    aStar.setHeuristicFunction(heuristicFunction);
    aStar.setTimeout(timeout);
    replay(aStar);

    // Run
    DefaultAStarSearchAlgorithmFactory factory=createInstance(aStar);
    factory.setFindingDao(findingDao);
    AStarSearchAlgorithm newAStar=factory.createAStarSearchAlgorithm(startNode,
      goalNode, costFunction, heuristicFunction, timeout, routingParameters);

    // Verify
    verify(aStar);
    assertSame(aStar, newAStar);
  }
}
