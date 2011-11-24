/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import java.util.Set;

import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author TineL
 */
public class BuildingConverter extends ConverterSupport {

  /**
   * Converts the Building from Domain object to client independent Data
   * Transfer Object (DTO).
   * 
   * @param building the building to convert (<code>null</code> not permitted)
   * @return a converted building (<code>null</code> not possible)
   */
  public BuildingDto convertBuilding(Building building) {
    DefaultBuildingDto dto=new DefaultBuildingDto();
    Set<GeoName> names=building.getGeoNames();
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
      case BUILDING:
        dto.setName(geoName.getName());
        break;
      default:
        break;
      }
    }
    dto.setNote(building.getNote());

    /*
     * Serialization problem: Coordinate must be newly created; old one is
     * org.hibernatespatial.mgeom.MCoordinate.
     */
    Polygon oldPolygon=building.getPolygon();
    Polygon newPolygon=deserializePolygon(oldPolygon);

    dto.setPolygon(newPolygon);
    return dto;
  }
}
