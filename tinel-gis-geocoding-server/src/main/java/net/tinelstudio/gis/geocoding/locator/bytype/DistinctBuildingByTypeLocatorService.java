/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tinelstudio.gis.common.dto.BuildingConverter;
import net.tinelstudio.gis.common.dto.BuildingDto;
import net.tinelstudio.gis.model.domain.Building;

/**
 * The implementation of {@link ByTypeLocatorService} that searches for
 * Buildings.
 * 
 * @author TineL
 */
public class DistinctBuildingByTypeLocatorService extends
  AbstractByTypeLocatorService {

  private BuildingConverter buildingConverter;

  @Override
  public List<BuildingDto> find() {
    // Phase I: Parsing
    String[] keys=parseSearchString();

    // Phase II: Elements assigned to keys
    List<String> names=Arrays.asList(keys);

    // Phase III: Searching
    List<Building> buildings=getFindingDao().findDistinctBuildingsByType(names,
      getType(), getMaxResults());

    // Phase IV: Converting
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
