/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import java.io.Serializable;

/**
 * Defines the locator. A locator is used to specify what geographical places
 * are to be searched for.
 * 
 * @author TineL
 */
public interface Locator extends Serializable {

  /**
   * @return the search string
   * @see #setSearchString(String)
   */
  String getSearchString();

  /**
   * The search string contains a string that is used by the search engine to
   * find exact or similar match of the geographical place's name (geoname).
   * 
   * @param searchString the search string (<code>null</code> not permitted)
   */
  void setSearchString(String searchString);

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