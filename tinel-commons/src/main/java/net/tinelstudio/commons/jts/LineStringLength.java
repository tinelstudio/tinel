/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.jts;

import net.tinelstudio.commons.DistanceOnEarth;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LineString;

/**
 * The utility class for computing a {@link LineString}'s length.
 * 
 * @author TineL
 */
public class LineStringLength {

  /**
   * Computes a metric length on Earth of the given line in meters using
   * {@link DistanceOnEarth}.
   * 
   * @param line the line to compute length of (<code>null</code> not permitted)
   * @return a length of the lines in meters or 0 if the line contains zero or
   *         only one coordinate
   * @see DistanceOnEarth
   */
  public static int computeLengthMeters(LineString line) {
    CoordinateSequence pts=line.getCoordinateSequence();
    /*
     * Copied & modified from
     * com.vividsolutions.jts.algorithm.CGAlgorithms.length(CoordinateSequence)
     */
    if (pts.size()<1) return 0;
    int sum=0;
    for (int i=1; i<pts.size(); i++) {
      Coordinate c=pts.getCoordinate(i);
      Coordinate cp=pts.getCoordinate(i-1);
      sum+=DistanceOnEarth.distance(c.y, c.x, cp.y, cp.x);
    }
    return sum;
  }
}
