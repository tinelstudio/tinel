/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.LineString;

/**
 * The default implementation of {@link StreetDto}.
 * 
 * @author TineL
 */
public class DefaultStreetDto extends AbstractPlace implements StreetDto {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-5717119736988700681L;

  private String name;
  private LineString lineString;
  private int lengthMeters;
  private Level level;
  private boolean oneWay;

  @Override
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name=name;
  }

  @Override
  public LineString getLineString() {
    return this.lineString;
  }

  public void setLineString(LineString lineString) {
    this.lineString=lineString;
  }

  @Override
  public int getLengthMeters() {
    return this.lengthMeters;
  }

  public void setLengthMeters(int lengthMeters) {
    this.lengthMeters=lengthMeters;
  }

  @Override
  public Level getLevel() {
    return this.level;
  }

  public void setLevel(Level level) {
    this.level=level;
  }

  @Override
  public boolean isOneWay() {
    return this.oneWay;
  }

  public void setOneWay(boolean oneWay) {
    this.oneWay=oneWay;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("DefaultStreetDto [name=").append(this.name).append(
      ", lengthMeters=").append(this.lengthMeters).append(", level=").append(
      this.level).append(", oneWay=").append(this.oneWay).append(
      ", getTownName()=").append(this.getTownName()).append(
      ", getRegionName()=").append(this.getRegionName()).append(
      ", getCountryName()=").append(this.getCountryName()).append(
      ", getContinentName()=").append(this.getContinentName()).append(
      ", getNote()=").append(this.getNote()).append("]");
    return builder.toString();
  }
}
