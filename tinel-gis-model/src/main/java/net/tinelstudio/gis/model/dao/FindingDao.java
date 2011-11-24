/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import java.util.List;

import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * Defines a Domain Access Object with special finding operations.
 * 
 * @author TineL
 */
public interface FindingDao {

  // #################
  // Findings by names

  /**
   * Finds addresses with the given names & house numbers.<br>
   * An address is found if it conforms to all names & house numbers specified.<br>
   * <br>
   * name1 AND ... AND nameN AND houseNumber1 AND ... AND houseNumberM <br>
   * <br>
   * If no names & house numbers are specified, all addresses within the given
   * maximum results limit are returned.<br>
   * 
   * @param names the list of address names (<code>null</code> permitted)
   * @param houseNumbers the list of house numbers (<code>null</code> permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found addresses (<code>null</code> not possible)
   */
  List<Address> findAddresses(List<String> names, List<String> houseNumbers,
    int maxResults);

  /**
   * Finds addresses with the given names of the specified type.<br>
   * An address is found if it conforms to the all given names that are of the
   * specified type.<br>
   * <br>
   * name1 AND ... AND nameN AND type
   * 
   * @param names the list of names (<code>null</code> not permitted)
   * @param type the type of the given names (<code>null</code> not permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found addresses (<code>null</code> not possible)
   */
  List<Address> findDistinctAddressesByType(List<String> names, Type type,
    int maxResults);

  /**
   * Finds streets with the given names.<br>
   * A street is found if it conforms to all names specified.<br>
   * <br>
   * name1 AND ... AND nameN
   * 
   * @param names the list of street names (<code>null</code> not permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found streets (<code>null</code> not possible)
   */
  List<Street> findStreets(List<String> names, int maxResults);

  /**
   * Finds streets with the given names of the specified type.<br>
   * A street is found if it conforms to the all given names that are of the
   * specified type.<br>
   * <br>
   * name1 AND ... AND nameN AND type
   * 
   * @param names the list of names (<code>null</code> not permitted)
   * @param type the type of the given names (<code>null</code> not permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found streets (<code>null</code> not possible)
   */
  List<Street> findDistinctStreetsByType(List<String> names, Type type,
    int maxResults);

  /**
   * Finds buildings with the given names.<br>
   * A building is found if it conforms to all names specified.<br>
   * <br>
   * name1 AND ... AND nameN
   * 
   * @param names the list of building names (<code>null</code> not permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found buildings (<code>null</code> not possible)
   */
  List<Building> findBuildings(List<String> names, int maxResults);

  /**
   * Finds buildings with the given names of the specified type.<br>
   * A building is found if it conforms to the all given names that are of the
   * specified type.<br>
   * <br>
   * name1 AND ... AND nameN AND type
   * 
   * @param names the list of names (<code>null</code> not permitted)
   * @param type the type of the given names (<code>null</code> not permitted)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a list of found buildings (<code>null</code> not possible)
   */
  List<Building> findDistinctBuildingsByType(List<String> names, Type type,
    int maxResults);

  // ################
  // Nearest findings

  /**
   * Finds addresses that are maximum of the given search distance away from the
   * given search location. Found addresses are sorted by the distance: the
   * nearest address is first.
   * 
   * @param point the search location (<code>null</code> not permitted)
   * @param maxDistanceMeters the maximum search distance in meters (must be >=
   *        0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of nearest addresses (<code>null</code> not possible)
   */
  List<Address> findNearestAddresses(Point point, int maxDistanceMeters,
    int maxResults);

  /**
   * Finds streets that are maximum of the given search distance away from the
   * given search location. Found streets are sorted by the distance: the
   * nearest street is first.
   * 
   * @param point the search location (<code>null</code> not permitted)
   * @param maxDistanceMeters the maximum search distance in meters (must be >=
   *        0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of nearest streets (<code>null</code> not possible)
   */
  List<Street> findNearestStreets(Point point, int maxDistanceMeters,
    int maxResults);

  /**
   * Finds buildings that are maximum of the given search distance away from the
   * given search location. Found buildings are sorted by the distance: the
   * nearest building is first.
   * 
   * @param point the search location (<code>null</code> not permitted)
   * @param maxDistanceMeters the maximum search distance in meters (must be >=
   *        0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of nearest buildings (<code>null</code> not possible)
   */
  List<Building> findNearestBuildings(Point point, int maxDistanceMeters,
    int maxResults);

  /**
   * Finds the nearest street node that is maximum of the given search distance
   * away from the given search location.
   * 
   * @param point the search location (<code>null</code> not permitted)
   * @param maxDistanceMeters the maximum search distance in meters (must be >=
   *        0)
   * @return the nearest street node or <code>null</code> none found
   */
  StreetNode findNearestStreetNode(Point point, int maxDistanceMeters);

  /**
   * Finds addresses that are approximately maximum of the given search distance
   * away from the given search place. Found addresses are approximately sorted
   * by the distance: the approximately nearest address is first.<br>
   * <br>
   * <b>Warning:</b> This search function is not accurate but is very fast.
   * 
   * @param place the search place (<code>null</code> not permitted)
   * @param maxDistanceMeters the approximate maximum search distance in meters
   *        (must be >= 0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of approximately nearest addresses (<code>null</code>
   *         not possible)
   */
  List<Address> findNearestAddressesApprox(Geometry place,
    int maxDistanceMeters, int maxResults);

  /**
   * Finds streets that are approximately maximum of the given search distance
   * away from the given search place. Found streets are approximately sorted by
   * the distance: the approximately nearest street is first.<br>
   * <br>
   * <b>Warning:</b> This search function is not accurate but is very fast.
   * 
   * @param place the search place (<code>null</code> not permitted)
   * @param maxDistanceMeters the approximate maximum search distance in meters
   *        (must be >= 0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of approximately nearest streets (<code>null</code>
   *         not possible)
   */
  List<Street> findNearestStreetsApprox(Geometry place, int maxDistanceMeters,
    int maxResults);

  /**
   * Finds buildings that are approximately maximum of the given search distance
   * away from the given search place. Found buildings are approximately sorted
   * by the distance: the approximately nearest building is first.<br>
   * <br>
   * <b>Warning:</b> This search function is not accurate but is very fast.
   * 
   * @param place the search place (<code>null</code> not permitted)
   * @param maxDistanceMeters the approximate maximum search distance in meters
   *        (must be >= 0)
   * @param maxResults the limit of maximum number of results to find or <= 0
   *        with no limit
   * @return a sorted list of approximately nearest buildings (<code>null</code>
   *         not possible)
   */
  List<Building> findNearestBuildingsApprox(Geometry place,
    int maxDistanceMeters, int maxResults);

  /**
   * Finds the approximate nearest street node that is approximately maximum of
   * the given search distance away from the given search place.<br>
   * <br>
   * <b>Warning:</b> This search function is not accurate but is very fast.
   * 
   * @param place the search place (<code>null</code> not permitted)
   * @param maxDistanceMeters the approximate maximum search distance in meters
   *        (must be >= 0)
   * @return an approximate nearest street node or <code>null</code> if none
   *         found
   */
  StreetNode findNearestStreetNodeApprox(Geometry place, int maxDistanceMeters);

  // ######################
  // Miscellaneous findings

  /**
   * Finds all streets that are connected to the given street node. The street
   * is connected if its start node or end node is the same as the given one. If
   * the street is one-way, then only its start node is considered.
   * 
   * @param streetNode the street node (<code>null</code> not permitted)
   * @return a list of connected streets (<code>null</code> not possible)
   */
  List<Street> findConnectedStreets(StreetNode streetNode);
}
