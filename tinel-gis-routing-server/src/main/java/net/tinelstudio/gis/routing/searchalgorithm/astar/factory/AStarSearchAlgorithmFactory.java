
package net.tinelstudio.gis.routing.searchalgorithm.astar.factory;

import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.RoutingParameters;
import net.tinelstudio.gis.routing.searchalgorithm.astar.AStarSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

/**
 * Defines the A* routing search algorithm factory.
 * 
 * @author TineL
 */
public interface AStarSearchAlgorithmFactory {

  /**
   * Creates a new instance of the A* routing search algorithm with the
   * specified properties.
   * 
   * @param startNode the start node (<code>null</code> not permitted)
   * @param goalNode the goal node (<code>null</code> not permitted)
   * @param costFunction the cost function g(n) (<code>null</code> not
   *        permitted)
   * @param originalRoutingParameters the original routing parameters that are
   *        given to the service (<code>null</code> not permitted)
   * @param heuristicFunction the heuristic function h(n) (<code>null</code> not
   *        permitted)
   * @param timeout the algorithm timeout in milliseconds. If <= 0, no timeout
   *        is defined.
   * @return a new instance of the search algorithm (<code>null</code> not
   *         possible)
   */
  AStarSearchAlgorithm createAStarSearchAlgorithm(StreetNode startNode,
    StreetNode goalNode, CostFunction costFunction,
    HeuristicFunction heuristicFunction, int timeout,
    RoutingParameters originalRoutingParameters);
}
