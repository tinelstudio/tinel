/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.db.dao;

import java.util.Collection;
import java.util.List;

/**
 * Defines a Domain Access Object (DAO) with typical load, save & delete
 * operations.
 * 
 * @author TineL
 * @param <E> the domain object
 */
public interface LoadSaveDeleteDao<E> {

  /**
   * Loads an object with the specified identifier from the DB if exists.
   * 
   * @param id the identifier of the object to load (<code>null</code> not
   *        permitted)
   * @return an object or <code>null</code> if none found
   */
  E load(Long id);

  /**
   * Loads all objects from the DB.
   * 
   * @return a list of all objects (<code>null</code> not possible)
   */
  List<E> loadAll();

  /**
   * Saves (persists) or updates the given object in the DB.
   * 
   * @param element the object to save (<code>null</code> not permitted)
   */
  void save(E element);

  /**
   * Saves (persists) or updates all the given objects in the DB.
   * 
   * @param elements the collection of the objects to save ( <code>null</code>
   *        not permitted)
   */
  void saveAll(Collection<E> elements);

  /**
   * Deletes an object with the specified identifier from the DB if exists.
   * 
   * @param id the identifier of the object to delete (<code>null</code> not
   *        permitted)
   * @return <code>false</code> if an object does not exist
   */
  boolean delete(Long id);

  /**
   * Deletes all objects from the DB.
   */
  void deleteAll();
}
