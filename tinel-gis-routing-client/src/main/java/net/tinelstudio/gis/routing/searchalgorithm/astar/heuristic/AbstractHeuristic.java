/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar.heuristic;

/**
 * The abstract implementation of {@link Heuristic}.
 * 
 * @author TineL
 */
public abstract class AbstractHeuristic implements Heuristic {

  private double weight=1.0;

  @Override
  public double getWeight() {
    return this.weight;
  }

  public void setWeight(double weight) {
    this.weight=weight;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append(" [weight=");
    builder.append(this.weight);
    builder.append("]");
    return builder.toString();
  }
}
