/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tinelstudio.gis.common.dto.StreetConverter;
import net.tinelstudio.gis.common.dto.StreetDto;
import net.tinelstudio.gis.model.domain.Street;

/**
 * The {@link StreetLocator} service.
 * 
 * @author TineL
 * @see StreetLocator
 */
public class StreetLocatorService extends AbstractLocatorService {

  private StreetConverter streetConverter;

  @Override
  public List<StreetDto> find() {
    // Phase I: Parse
    String[] keys=parseSearchString();

    // Phase II: Elements assigned to keys
    List<String> names=Arrays.asList(keys);

    // Phase III: Search
    List<Street> streets=getFindingDao().findStreets(names, getMaxResults());

    // Phase IV: Convert
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
