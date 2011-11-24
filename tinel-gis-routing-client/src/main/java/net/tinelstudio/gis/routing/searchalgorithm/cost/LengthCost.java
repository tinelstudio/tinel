/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

/**
 * The cost function that is defined only with the length of the street, that is
 * between node n-1 and node n.
 * 
 * @author TineL
 * @see Cost
 */
public class LengthCost implements Cost {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-1835145998968235097L;

  @Override
  public String toString() {
    return "LengthCost []";
  }
}
