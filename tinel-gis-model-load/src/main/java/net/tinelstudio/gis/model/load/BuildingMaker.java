/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Polygon;

/**
 * @author TineL
 */
@Component
public class BuildingMaker extends InMemoryGeoNameMaker {

  public Building createBuilding(Polygon polygon, String continent,
    String country, String region, String town, String name, String note) {
    Building b=new Building();

    // Set GeoNames
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
      geoNames.add(findOrCreateGeoName(name, Type.BUILDING));
    }
    b.setGeoNames(geoNames);

    b.setPolygon(polygon);

    b.setNote(note);

    return b;
  }
}
