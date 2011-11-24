/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Defines the locator. A locator is used to specify what geographical places
 * are to be searched for.
 * 
 * @author TineL
 */
public interface Locator extends Serializable {

  /**
   * @return the search location
   * @see #setSearchLocation(Coordinate)
   */
  Coordinate getSearchLocation();

  /**
   * The search location is a location coordinate around which is searched for
   * the existing geographical places.
   * 
   * @param location the search location (<code>null</code> not permitted)
   */
  void setSearchLocation(Coordinate location);

  /**
   * @return the maximum distance of the search radius in meters (-1 if
   *         undefined)
   */
  int getMaxDistanceMeters();

  /**
   * The maximum distance specifies a maximum search radius in which is searched
   * for the geographical places.
   * 
   * @param maxDistanceMeters the maximum distance of the search radius in
   *        meters (must be >= 0)
   */
  void setMaxDistanceMeters(int maxDistanceMeters);

  /**
   * @return the maximum results to be found
   * @see #setMaxResults(int)
   */
  int getMaxResults();

  /**
   * @param maxResults the maximum results to be found or <= 0 if no limit
   */
  void setMaxResults(int maxResults);
}
