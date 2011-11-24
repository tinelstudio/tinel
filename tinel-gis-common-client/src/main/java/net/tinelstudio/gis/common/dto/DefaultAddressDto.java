/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.Point;

/**
 * The default implementation of {@link AddressDto}.
 * 
 * @author TineL
 */
public class DefaultAddressDto extends AbstractPlace implements AddressDto {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=3189169917725311652L;

  private String name;
  private String houseNumber;
  private Point point;

  @Override
  public Point getPoint() {
    return this.point;
  }

  public void setPoint(Point point) {
    this.point=point;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public void setName(String streetName) {
    this.name=streetName;
  }

  @Override
  public String getHouseNumber() {
    return this.houseNumber;
  }

  public void setHouseNumber(String houseNumber) {
    this.houseNumber=houseNumber;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("DefaultAddressDto [name=").append(this.name).append(
      ", houseNumber=").append(this.houseNumber).append(", getTownName()=")
      .append(this.getTownName()).append(", getRegionName()=").append(
        this.getRegionName()).append(", getCountryName()=").append(
        this.getCountryName()).append(", getContinentName()=").append(
        this.getContinentName()).append(", getNote()=").append(this.getNote())
      .append("]");
    return builder.toString();
  }
}
