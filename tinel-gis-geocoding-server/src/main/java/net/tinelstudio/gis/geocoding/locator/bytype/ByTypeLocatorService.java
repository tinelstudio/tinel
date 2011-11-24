/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import net.tinelstudio.gis.geocoding.locator.LocatorService;
import net.tinelstudio.gis.model.domain.GeoName.Type;

/**
 * Defines a {@link LocatorService} that searches for geographical names
 * (geonames) by the specified {@link Type}.
 * 
 * @author TineL
 */
public interface ByTypeLocatorService extends LocatorService {

  /**
   * @param type the type to search by (<code>null</code> not permitted)
   */
  void setType(Type type);

  /**
   * @return the type to search by
   */
  Type getType();
}
