/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import java.util.Set;

import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.GeoName;

import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class AddressConverter extends ConverterSupport {

  /**
   * Converts the Address from Domain object to client independent Data Transfer
   * Object (DTO).
   * 
   * @param address the address to convert (<code>null</code> not permitted)
   * @return a converted address (<code>null</code> not possible)
   */
  public AddressDto convertAddress(Address address) {
    DefaultAddressDto dto=new DefaultAddressDto();
    Set<GeoName> geoNames=address.getGeoNames();
    for (GeoName geoName : geoNames) {
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
      case ADDRESS:
        dto.setName(geoName.getName());
        break;

      default:
        break;
      }
    }
    dto.setHouseNumber(address.getHouseNumber());
    dto.setNote(address.getNote());

    /*
     * Serialization problem: Coordinate must be newly created; old one is
     * org.hibernatespatial.mgeom.MCoordinate.
     */
    Point oldPoint=address.getPoint();
    Point newPoint=deserializePoint(oldPoint);

    dto.setPoint(newPoint);
    return dto;
  }
}
