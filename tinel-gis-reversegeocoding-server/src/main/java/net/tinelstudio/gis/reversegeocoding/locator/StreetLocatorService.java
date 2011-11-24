/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.common.dto.StreetConverter;
import net.tinelstudio.gis.common.dto.StreetDto;
import net.tinelstudio.gis.model.domain.Street;

import com.vividsolutions.jts.geom.Point;

/**
 * The {@link StreetLocator} service.
 * 
 * @author TineL
 * @see StreetLocator
 */
public class StreetLocatorService extends AbstractLocatorService {

  private StreetConverter streetConverter;

  @Override
  public List<? extends Place> findNearest() {
    // Phase I: Prepare
    Point point=convertToPoint(getSearchLocation());

    // Phase II: Search
    List<Street> streets=getFindingDao().findNearestStreets(point,
      getMaxDistanceMeters(), getMaxResults());

    // Phase III: Convert
    List<StreetDto> dtos=new ArrayList<StreetDto>(streets.size());
    for (Street s : streets) {
      StreetDto dto=getStreetConverter().convertStreet(s);
      dtos.add(dto);
    }
    return dtos;
  }

  public StreetConverter getStreetConverter() {
    if (this.streetConverter==null) {
      this.streetConverter=new StreetConverter();
    }
    return this.streetConverter;
  }

  public void setStreetConverter(StreetConverter streetConverter) {
    this.streetConverter=streetConverter;
  }
}
