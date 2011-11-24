/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
@Component
public class AddressMaker extends InMemoryGeoNameMaker {

  @Autowired
  private GeometryFactory geometryFactory;

  public Address createAddress(double longitude, double latitude,
    String continent, String country, String region, String town, String name,
    String houseNumber, String note) {
    Address a=new Address();

    // Create point
    Coordinate c=new Coordinate(longitude, latitude);
    Point point=getGeometryFactory().createPoint(c);
    a.setPoint(point);

    // Set names
    Set<GeoName> geoNames=new HashSet<GeoName>();
    if (continent!=null) {
      geoNames.add(findOrCreateGeoName(continent, Type.CONTINENT));
    }
    if (country!=null) {
      geoNames.add(findOrCreateGeoName(country, Type.COUNTRY));
    }
    if (region!=null) {
      geoNames.add(findOrCreateGeoName(region, Type.REGION));
    }
    if (town!=null) {
      geoNames.add(findOrCreateGeoName(town, Type.TOWN));
    }
    if (name!=null) {
      geoNames.add(findOrCreateGeoName(name, Type.ADDRESS));
    }
    a.setGeoNames(geoNames);

    // Set house
    a.setHouseNumber(houseNumber);

    // Set note
    a.setNote(note);

    return a;
  }

  public GeometryFactory getGeometryFactory() {
    return this.geometryFactory;
  }

  public void setGeometryFactory(GeometryFactory geometryFactory) {
    this.geometryFactory=geometryFactory;
  }
}