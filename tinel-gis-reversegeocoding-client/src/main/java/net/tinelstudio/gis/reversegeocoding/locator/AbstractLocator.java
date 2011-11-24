/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The abstract implementation of {@link Locator}.
 * 
 * @author TineL
 */
public class AbstractLocator implements Locator {

  private Coordinate searchLocation;
  private int maxDistanceMeters=-1;
  private int maxResults=-1;

  @Override
  public Coordinate getSearchLocation() {
    return this.searchLocation;
  }

  @Override
  public void setSearchLocation(Coordinate location) {
    this.searchLocation=location;
  }

  @Override
  public int getMaxDistanceMeters() {
    return this.maxDistanceMeters;
  }

  @Override
  public void setMaxDistanceMeters(int maxDistanceMeters) {
    this.maxDistanceMeters=maxDistanceMeters;
  }

  @Override
  public int getMaxResults() {
    return this.maxResults;
  }

  @Override
  public void setMaxResults(int maxResults) {
    this.maxResults=maxResults;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append(" [searchLocation=");
    builder.append(this.searchLocation);
    builder.append(", maxDistanceMeters=");
    builder.append(this.maxDistanceMeters);
    builder.append(", maxResults=");
    builder.append(this.maxResults);
    builder.append("]");
    return builder.toString();
  }
}
