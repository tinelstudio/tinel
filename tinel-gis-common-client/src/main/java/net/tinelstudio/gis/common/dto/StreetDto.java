/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.LineString;

/**
 * Defines the Street transfer object (DTO).
 * 
 * @author TineL
 */
public interface StreetDto extends Place {

  String getName();

  /**
   * @return the spatial representation of the street
   */
  LineString getLineString();

  /**
   * @return the length of the street in meters
   */
  int getLengthMeters();

  /**
   * The street levels.
   * 
   * @author TineL
   */
  public enum Level {

    /** Unknown street level. */
    UNKNOWN,

    /** High speed streets (highways) with usually more lanes. */
    MAJOR,

    /** Normal speed streets. */
    MEDIUM,

    /** Low speed tight streets, usually in the cities. */
    MINOR
  }

  Level getLevel();

  /**
   * @return <code>true</code> if this street is one-way
   */
  boolean isOneWay();
}
