/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic.Heuristic;
import net.tinelstudio.gis.routing.searchalgorithm.cost.Cost;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The simple implementation of the {@link RoutingParameters} that allows all
 * parameters to be custom set.
 * 
 * @author TineL
 */
public class CustomRoutingParameters implements RoutingParameters {

  private Coordinate startLocation;
  private Coordinate goalLocation;
  private Cost cost;
  private Heuristic heuristic;

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
    return this.cost;
  }

  public void setCost(Cost cost) {
    this.cost=cost;
  }

  @Override
  public Heuristic getHeuristic() {
    return this.heuristic;
  }

  public void setHeuristic(Heuristic heuristic) {
    this.heuristic=heuristic;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("CustomRoutingParameters [startLocation=");
    builder.append(this.startLocation);
    builder.append(", goalLocation=");
    builder.append(this.goalLocation);
    builder.append(", cost=");
    builder.append(this.cost);
    builder.append(", heuristic=");
    builder.append(this.heuristic);
    builder.append("]");
    return builder.toString();
  }
}
