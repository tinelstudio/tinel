/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import java.util.Set;

import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.Street;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author TineL
 */
public class StreetConverter extends ConverterSupport {

  /**
   * Converts the Street from Domain object to client independent Data Transfer
   * Object (DTO).
   * 
   * @param street the street to convert (<code>null</code> not permitted)
   * @return a converted street (<code>null</code> not possible)
   */
  public StreetDto convertStreet(Street street) {
    DefaultStreetDto dto=new DefaultStreetDto();
    Set<GeoName> names=street.getGeoNames();
    for (GeoName geoName : names) {
      switch (geoName.getType()) {
      case CONTINENT:
        dto.setContinentName(geoName.getName());
        break;
      case COUNTRY:
        dto.setCountryName(geoName.getName());
        break;
      case REGION:
        dto.setRegionName(geoName.getName());
        break;
      case TOWN:
        dto.setTownName(geoName.getName());
        break;
      case STREET:
        dto.setName(geoName.getName());
        break;
      default:
        break;
      }
    }

    dto.setLengthMeters(street.getLengthMeters());
    dto.setLevel(street.getLevel());
    dto.setOneWay(street.getOneWay());
    dto.setNote(street.getNote());

    /*
     * Serialization problem: Coordinate must be newly created; old one is
     * org.hibernatespatial.mgeom.MCoordinate.
     */
    LineString oldLine=street.getLineString();
    LineString newLine=deserializeLineString(oldLine);

    dto.setLineString(newLine);
    return dto;
  }
}
