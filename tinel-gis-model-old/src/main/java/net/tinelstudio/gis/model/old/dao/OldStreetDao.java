/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.dao;

import java.util.List;

import net.tinelstudio.gis.model.old.domain.OldStreet;

/**
 * @author TineL
 */
public interface OldStreetDao {

  /**
   * Loads all objects from the DB.
   * 
   * @param oldEntityClass the entity class (<code>null</code> not permitted)
   * @return a list of all objects (<code>null</code> not possible)
   */
  List<? extends OldStreet> loadAll(Class<? extends OldStreet> oldEntityClass);

  /**
   * Deletes an object with the specified identifier from the DB if exists.
   * 
   * @param oldEntityClass the entity class (<code>null</code> not permitted)
   * @param id the identifier of the object to delete (<code>null</code> not
   *        permitted)
   * @return <code>false</code> if an object does not exist
   */
  boolean delete(Class<? extends OldStreet> oldEntityClass, Long id);
}