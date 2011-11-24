/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import java.util.HashMap;
import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.GreatCircleDistanceHeuristic;
import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;
import net.tinelstudio.gis.routing.searchalgorithm.cost.LevelLengthCost;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The default implementation of the {@link RoutingParameters} with the default
 * {@link Heuristic} and {@link Cost} functions.
 * 
 * @author TineL
 */
public class DefaultRoutingParameters implements RoutingParameters {

  protected static final Cost cost;
  protected static final Heuristic heuristic;

  static {
    // Create default level/weight mapped cost function
    Map<Level, Double> speedMap=new HashMap<Level, Double>();
    speedMap.put(Level.UNKNOWN, 90/70.0);
    speedMap.put(Level.MAJOR, 90/110.0);
    speedMap.put(Level.MEDIUM, 90/65.0);
    speedMap.put(Level.MINOR, 90/55.0);
    cost=new LevelLengthCost(speedMap);

    // Create default heuristic function
    heuristic=new GreatCircleDistanceHeuristic(1.24);
  }

  private Coordinate startLocation;
  private Coordinate goalLocation;

  @Override
  public Coordinate getStartLocation() {
    return this.startLocation;
  }

  public void setStartLocation(Coordinate startLocation) {
    this.startLocation=startLocation;
  }

  @Override
  public Coordinate getGoalLocation() {
    return this.goalLocation;
  }

  public void setGoalLocation(Coordinate goalLocation) {
    this.goalLocation=goalLocation;
  }

  @Override
  public Cost getCost() {
    return cost;
  }

  @Override
  public Heuristic getHeuristic() {
    return heuristic;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("DefaultRoutingParameters [startLocation=");
    builder.append(this.startLocation);
    builder.append(", goalLocation=");
    builder.append(this.goalLocation);
    builder.append(", cost=");
    builder.append(cost);
    builder.append(", heuristic=");
    builder.append(heuristic);
    builder.append("]");
    return builder.toString();
  }
}
