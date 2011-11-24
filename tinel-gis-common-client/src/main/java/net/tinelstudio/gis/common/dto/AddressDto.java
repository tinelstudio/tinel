/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.Point;

/**
 * Defines the Address transfer object (DTO).
 * 
 * @author TineL
 */
public interface AddressDto extends Place {

  /**
   * @return the name of the address, usually street name
   */
  String getName();

  /**
   * @return the house number (alphanumeric) of the address
   */
  String getHouseNumber();

  /**
   * @return the spatial representation of the address
   */
  Point getPoint();
}
