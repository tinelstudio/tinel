/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.searchalgorithm.AbstractSearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.SearchAlgorithm;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.HeuristicFunction;

import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * The abstract A* routing search algorithm.<br>
 * <br>
 * <b>Warning:</b> Create the implementation of this object anew for every run.
 * Do not reuse it.<br>
 * <br>
 * The algorithm summarizes the <a href=
 * "http://theory.stanford.edu/~amitp/GameProgramming/ImplementationNotes.html"
 * >Amit's A* pseudo code</a>.
 * 
 * @author TineL
 * @see SearchAlgorithm
 * @see <a href="http://theory.stanford.edu/~amitp/GameProgramming/">Amit’s
 *      APages</a>
 * @author TineL
 */
public abstract class AbstractAStarSearchAlgorithm extends
  AbstractSearchAlgorithm implements AStarSearchAlgorithm {

  private HeuristicFunction heuristicFunction;

  private int findConnectedStreetsCalls=0; // findConnectedStreets call counter

  /**
   * A {@link StreetNode} decorator with g & h values. For internal use.
   * 
   * @author TineL
   */
  protected static class StreetNodeDecorator {

    private StreetNode streetNode;

    public double g;

    public double h;

    public StreetNodeDecorator(StreetNode streetNode) {
      this.streetNode=streetNode;
    }

    public Long getId() {
      return getStreetNode().getId();
    }

    public Point getPoint() {
      return getStreetNode().getPoint();
    }

    public StreetNode getStreetNode() {
      return this.streetNode;
    }
  }

  @Override
  public Route run() throws TimeoutException {
    Assert.notNull(getStartNode(), "Start node cannot be null");
    Assert.notNull(getGoalNode(), "Goal node cannot be null");
    Assert.notNull(getCostFunction(), "Cost function cannot be null");
    Assert.notNull(getHeuristicFunction(), "Heuristic function cannot be null");

    if (logger.isInfoEnabled()) {
      logger.info("Running A* search with startNode="+getStartNode()
        +", goalNode="+getGoalNode()+", costFunction="+getCostFunction()
        +", heuristicFunction="+getHeuristicFunction()+", timeout="
        +getTimeout());
    }

    long st=System.currentTimeMillis();

    StreetNodeDecorator startN=new StreetNodeDecorator(getStartNode());
    StreetNodeDecorator goalN=new StreetNodeDecorator(getGoalNode());

    HashMap<Long, StreetNodeDecorator> parentMap=new HashMap<Long, StreetNodeDecorator>();
    HashMap<Long, Street> streetMap=new HashMap<Long, Street>();
    HashMap<Long, StreetNodeDecorator> openSet=new HashMap<Long, StreetNodeDecorator>();
    HashMap<Long, StreetNodeDecorator> closedSet=new HashMap<Long, StreetNodeDecorator>();

    startN.g=0.0;
    startN.h=h(startN.getPoint());
    openSet.put(startN.getId(), startN);

    while (true) {
      // Select a good element of OPEN
      StreetNodeDecorator current=null;
      for (StreetNodeDecorator node : openSet.values()) {
        if (current==null||node.h+node.g<current.h+current.g) {
          current=node;
        }
      }

      // While lowest rank in OPEN is not the GOAL
      if (current==null||current.getId().equals(goalN.getId())) {
        // Finished
        break;
      }

      openSet.remove(current.getId());
      closedSet.put(current.getId(), current);

      // For neighbors of current
      Collection<Street> neighbors=findConnectedStreets(current.getStreetNode());
      this.findConnectedStreetsCalls++;
      if (getTimeout()>0) {
        // Check if time is out
        long ct=System.currentTimeMillis();
        if (ct-st>=getTimeout()) throw new TimeoutException();
      }

      for (Street street : neighbors) {
        StreetNodeDecorator neighbor;
        // Get node on the other side of current
        if (current.getId().equals(street.getStartNode().getId())) {
          neighbor=new StreetNodeDecorator(street.getEndNode());

        } else {
          neighbor=new StreetNodeDecorator(street.getStartNode());
        }

        double cost=current.g+computeCost(street);

        StreetNodeDecorator openNode=openSet.get(neighbor.getId());

        // Neighbor cannot be in OPEN and CLOSED simultaneously

        if (openNode!=null) {
          // Neighbor is in OPEN

          // Load neighbor
          neighbor=openNode;

          if (cost>=neighbor.g) {
            // Leave neighbor in OPEN for now, because old path is better
            continue;

          } else {
            // Remove neighbor from OPEN, because new path is better
            /*
             * Doesn't really need to remove neighbor from OPEN since it will be
             * updated after this if-clause
             */
          }

        } else {
          // Neighbor is not in OPEN

          StreetNodeDecorator closedNode=closedSet.get(neighbor.getId());
          if (closedNode!=null) {
            // Neighbor is in CLOSED

            // Load neighbor
            neighbor=closedNode;

            if (cost>=neighbor.g) {
              // Leave neighbor in CLOSED, because this path is better
              continue;

            } else {
              /*
               * This should never happen if you have an admissible heuristic.
               * However in games we often have inadmissible heuristics. By
               * Amit.
               */

              // Remove neighbor from CLOSED
              closedSet.remove(neighbor.getId());

              // Neighbor will be put into OPEN again
            }
          }
        }

        // Put/update neighbor in OPEN
        neighbor.g=cost;
        neighbor.h=h(neighbor.getPoint());
        openSet.put(neighbor.getId(), neighbor);
        parentMap.put(neighbor.getId(), current);
        streetMap.put(neighbor.getId(), street);
        if (neighbor.getId().equals(goalN.getId())) {
          // Last
          goalN=neighbor;
        }
      }
    }

    List<Street> streets=reconstructPath(parentMap, streetMap, goalN);

    long et=System.currentTimeMillis();
    if (logger.isInfoEnabled()) {
      logger.info("Run time: "+(et-st)+" ms");
      logger.info("Connected streets finds: "+this.findConnectedStreetsCalls);
    }

    if (streets==null) return null;
    Coordinate startCoordinate=getStartNode().getPoint().getCoordinate();
    Route route=createRoute(streets, startCoordinate, goalN.g, et-st);
    return route;
  }

  /**
   * Finds all streets that are connected to the given street node. The street
   * is connected if its start node or end node is the same as the given one. If
   * the street is one-way, then only its start node is considered.
   * 
   * @param streetNode the street node (<code>null</code> not permitted)
   * @return a list of connected streets (<code>null</code> not possible)
   */
  protected abstract Collection<Street> findConnectedStreets(
    StreetNode streetNode);

  /**
   * Reconstructs the route path.
   * 
   * @param parentMap the map of node parents: parent node ID -> child node (
   *        <code>null</code> not permitted)
   * @param streetMap the map of street IDs: node ID -> street (
   *        <code>null</code> not permitted)
   * @param goal the goal node (<code>null</code> not permitted)
   * @return a list of streets or <code>null</code> if path is not connected to
   *         goal node
   */
  protected List<Street> reconstructPath(
    Map<Long, StreetNodeDecorator> parentMap, Map<Long, Street> streetMap,
    StreetNodeDecorator goal) {
    Street lastStreet=streetMap.get(goal.getId());
    if (lastStreet==null) {
      // No street found: route not found
      return null;
    }

    LinkedList<Street> constr=new LinkedList<Street>();
    constr.addFirst(lastStreet);
    StreetNodeDecorator current=parentMap.get(goal.getId());
    while (current!=null) {
      Street s=streetMap.get(current.getId());
      if (s!=null) { // Only first street is null
        constr.addFirst(s);
      }
      current=parentMap.get(current.getId());
    }
    return constr;
  }

  /**
   * Computes heuristic h(n) between the given point and the goal.
   * 
   * @param point the point n (<code>null</code> not permitted)
   * @return a heuristic
   */
  protected double h(Point point) {
    Point goalPoint=getGoalNode().getPoint();
    return getHeuristicFunction().computeHeuristic(point.getCoordinate(),
      goalPoint.getCoordinate());
  }

  @Override
  public HeuristicFunction getHeuristicFunction() {
    return this.heuristicFunction;
  }

  public void setHeuristicFunction(HeuristicFunction heuristicFunction) {
    this.heuristicFunction=heuristicFunction;
  }
}
