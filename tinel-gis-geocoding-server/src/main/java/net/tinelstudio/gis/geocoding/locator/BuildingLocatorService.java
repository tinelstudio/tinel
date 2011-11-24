/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tinelstudio.gis.common.dto.BuildingConverter;
import net.tinelstudio.gis.common.dto.BuildingDto;
import net.tinelstudio.gis.model.domain.Building;

/**
 * The {@link BuildingLocator} service.
 * 
 * @author TineL
 * @see BuildingLocator
 */
public class BuildingLocatorService extends AbstractLocatorService {

  private BuildingConverter buildingConverter;

  @Override
  public List<BuildingDto> find() {
    // Phase I: Parse
    String[] keys=parseSearchString();

    // Phase II: Elements assigned to keys
    List<String> names=Arrays.asList(keys);

    // Phase III: Search
    List<Building> buildings=getFindingDao().findBuildings(names,
      getMaxResults());

    // Phase IV: Convert
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
