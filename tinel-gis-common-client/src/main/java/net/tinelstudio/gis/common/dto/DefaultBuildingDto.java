/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.Polygon;

/**
 * The default implementation of {@link BuildingDto}.
 * 
 * @author TineL
 */
public class DefaultBuildingDto extends AbstractPlace implements BuildingDto {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=205794601970668250L;

  private String name;
  private Polygon polygon;

  @Override
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name=name;
  }

  @Override
  public Polygon getPolygon() {
    return this.polygon;
  }

  public void setPolygon(Polygon building) {
    this.polygon=building;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("DefaultBuildingDto [name=").append(this.name).append(
      ", getTownName()=").append(this.getTownName()).append(
      ", getRegionName()=").append(this.getRegionName()).append(
      ", getCountryName()=").append(this.getCountryName()).append(
      ", getContinentName()=").append(this.getContinentName()).append(
      ", getNote()=").append(this.getNote()).append("]");
    return builder.toString();
  }
}
