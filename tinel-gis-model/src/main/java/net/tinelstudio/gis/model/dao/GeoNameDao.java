/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

/**
 * The GeoName Domain Access Object (DAO) with typical load, save & delete
 * operations.
 * 
 * @author TineL
 */
public interface GeoNameDao extends LoadSaveDeleteDao<GeoName> {

  /**
   * Finds & loads a GeoName from the DB if exists.
   * 
   * @param name the name of the GeoName (<code>null</code> not permitted)
   * @param type the type of the GeoName (<code>null</code> not permitted)
   * @return a GeoName or <code>null</code> if none found
   */
  GeoName load(String name, Type type);
}