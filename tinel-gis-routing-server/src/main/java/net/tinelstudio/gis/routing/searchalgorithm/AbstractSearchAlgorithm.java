/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tinelstudio.gis.common.dto.StreetConverter;
import net.tinelstudio.gis.common.dto.StreetDto;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.DefaultRoute;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The abstract implementation of {@link SearchAlgorithm}.
 * 
 * @author TineL
 */
public abstract class AbstractSearchAlgorithm implements SearchAlgorithm {

  protected final Log logger=LogFactory.getLog(getClass());

  private StreetNode startNode;
  private StreetNode goalNode;
  private CostFunction costFunction;
  private int timeout;

  private StreetConverter streetConverter;

  /**
   * Creates a route out of the given parameters.
   * 
   * @param streets the streets (<code>null</code> not permitted)
   * @param startCoordinate the coordinate of the route start (<code>null</code>
   *        not permitted)
   * @param cost the cost
   * @param timeTaken the time taken
   * @return a route (<code>null</code> not possible)
   */
  protected Route createRoute(List<Street> streets, Coordinate startCoordinate,
    double cost, long timeTaken) {
    // Convert
    List<StreetDto> dtos=new ArrayList<StreetDto>(streets.size());
    for (Street s : streets) {
      StreetDto dto=getStreetConverter().convertStreet(s);
      dtos.add(dto);
    }
    /*
     * Create new clean coordinate, because given is actually non-serializable
     * org.hibernatespatial.mgeom.MCoordinate
     */
    Coordinate cleanStartCoordinate=new Coordinate(startCoordinate);

    // Create
    DefaultRoute route=new DefaultRoute();
    route.setStreets(Collections.unmodifiableList(dtos));
    route.setStartCoordinate(cleanStartCoordinate);
    route.setCost(cost);
    route.setTimeTaken(timeTaken);
    return route;
  }

  /**
   * Computes cost of the given street using {@link #getCostFunction()}.
   * 
   * @param street the street (<code>null</code> not permitted)
   * @return a cost of the street
   */
  protected double computeCost(Street street) {
    return getCostFunction().computeCost(street);
  }

  @Override
  public CostFunction getCostFunction() {
    return this.costFunction;
  }

  public void setCostFunction(CostFunction costFunction) {
    this.costFunction=costFunction;
  }

  @Override
  public StreetNode getStartNode() {
    return this.startNode;
  }

  public void setStartNode(StreetNode startNode) {
    this.startNode=startNode;
  }

  @Override
  public StreetNode getGoalNode() {
    return this.goalNode;
  }

  public void setGoalNode(StreetNode goalNode) {
    this.goalNode=goalNode;
  }

  @Override
  public int getTimeout() {
    return this.timeout;
  }

  public void setTimeout(int timeout) {
    this.timeout=timeout;
  }

  public StreetConverter getStreetConverter() {
    if (this.streetConverter==null) {
      this.streetConverter=new StreetConverter();
    }
    return this.streetConverter;
  }

  public void setStreetConverter(StreetConverter streetConverter) {
    this.streetConverter=streetConverter;
  }
}
