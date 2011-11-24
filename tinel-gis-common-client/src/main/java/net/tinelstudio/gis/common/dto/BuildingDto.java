/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.Polygon;

/**
 * Defines the Building transfer object (DTO).
 * 
 * @author TineL
 */
public interface BuildingDto extends Place {

  String getName();

  /**
   * @return the spatial representation of the building
   */
  Polygon getPolygon();
}
