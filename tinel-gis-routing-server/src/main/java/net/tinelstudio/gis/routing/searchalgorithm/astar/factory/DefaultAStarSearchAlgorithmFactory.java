
package net.tinelstudio.gis.routing.searchalgorithm.astar.factory;

import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.RoutingParameters;
import net.tinelstudio.gis.routing.searchalgorithm.astar.AStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.DefaultAStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The default A* routing search algorithm factory.
 * 
 * @author TineL
 */
public class DefaultAStarSearchAlgorithmFactory implements
  AStarSearchAlgorithmFactory, InitializingBean {

  private FindingDao findingDao;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(getFindingDao());
  }

  @Override
  public AStarSearchAlgorithm createAStarSearchAlgorithm(StreetNode startNode,
    StreetNode goalNode, CostFunction costFunction,
    HeuristicFunction heuristicFunction, int timeout,
    RoutingParameters originalRoutingParameters) {
    DefaultAStarSearchAlgorithm aStar=createAStarSearchAlgorithm();
    aStar.setFindingDao(getFindingDao());
    aStar.setStartNode(startNode);
    aStar.setGoalNode(goalNode);
    aStar.setCostFunction(costFunction);
    aStar.setHeuristicFunction(heuristicFunction);
    aStar.setTimeout(timeout);
    return aStar;
  }

  protected DefaultAStarSearchAlgorithm createAStarSearchAlgorithm() {
    return new DefaultAStarSearchAlgorithm();
  }

  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }
}
