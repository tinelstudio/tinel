/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.BuildingConverter;
import net.tinelstudio.gis.common.dto.BuildingDto;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.model.domain.Building;

import com.vividsolutions.jts.geom.Point;

/**
 * The {@link BuildingLocator} service.
 * 
 * @author TineL
 * @see BuildingLocator
 */
public class BuildingLocatorService extends AbstractLocatorService {

  private BuildingConverter buildingConverter;

  @Override
  public List<? extends Place> findNearest() {
    // Phase I: Prepare
    Point point=convertToPoint(getSearchLocation());

    // Phase II: Search
    List<Building> buildings=getFindingDao().findNearestBuildings(point,
      getMaxDistanceMeters(), getMaxResults());

    // Phase III: Convert
    List<BuildingDto> dtos=new ArrayList<BuildingDto>(buildings.size());
    for (Building b : buildings) {
      BuildingDto dto=getBuildingConverter().convertBuilding(b);
      dtos.add(dto);
    }
    return dtos;
  }

  public BuildingConverter getBuildingConverter() {
    if (this.buildingConverter==null) {
      this.buildingConverter=new BuildingConverter();
    }
    return this.buildingConverter;
  }

  public void setBuildingConverter(BuildingConverter buildingConverter) {
    this.buildingConverter=buildingConverter;
  }
}
