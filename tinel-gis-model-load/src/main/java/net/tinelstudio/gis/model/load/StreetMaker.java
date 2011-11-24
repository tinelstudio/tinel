/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.commons.DistanceOnEarth;
import net.tinelstudio.commons.jts.LineStringLength;
import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.domain.AddressRange;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;
import net.tinelstudio.gis.model.domain.Street;

import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
@Component
public class StreetMaker extends InMemoryStreetNodeMaker {

  public Street createStreet(LineString lineString, String continent,
    String country, String region, String town, String street, boolean oneWay,
    int lengthMeters, Level level, String note, Point startPoint, Point endPoint) {
    Street s=new Street();

    // Set names
    Set<GeoName> geoNames=new HashSet<GeoName>();
    if (continent!=null) {
      geoNames.add(findOrCreateGeoName(continent, Type.CONTINENT));
    }
    if (country!=null) {
      geoNames.add(findOrCreateGeoName(country, Type.COUNTRY));
    }
    if (region!=null) {
      geoNames.add(findOrCreateGeoName(region, Type.REGION));
    }
    if (town!=null) {
      geoNames.add(findOrCreateGeoName(town, Type.TOWN));
    }
    if (street!=null) {
      geoNames.add(findOrCreateGeoName(street, Type.STREET));
    }
    s.setGeoNames(geoNames);

    // Set shape
    s.setLineString(lineString);

    s.setOneWay(oneWay);

    s.setLengthMeters(lengthMeters);

    s.setLevel(level);

    s.setNote(note);

    s.setStartNode(findOrCreateStreetNode(startPoint));

    s.setEndNode(findOrCreateStreetNode(endPoint));

    return s;
  }

  /**
   * Creates a smart copy of the given street. The copy has id=null. The
   * collections are new instances with the same content.
   * 
   * @param street the street to copy (<code>null</code> not permitted)
   * @return a smart copy of the street (<code>null</code> not possible)
   */
  public Street newStreet(Street street) {
    Street s=new Street();

    Set<GeoName> geoNames=street.getGeoNames();
    if (geoNames!=null&&!geoNames.isEmpty()) {
      s.setGeoNames(new HashSet<GeoName>(geoNames));
    }

    s.setOneWay(street.getOneWay());

    s.setLengthMeters(street.getLengthMeters());

    s.setLevel(street.getLevel());

    s.setNote(street.getNote());

    s.setStartNode(street.getStartNode());

    s.setEndNode(street.getEndNode());

    Set<AddressRange> lar=street.getLeftAddressRanges();
    if (lar!=null&&!lar.isEmpty()) {
      s.setLeftAddressRanges(new HashSet<AddressRange>(lar));
    }

    Set<AddressRange> rar=street.getRightAddressRanges();
    if (rar!=null&&!rar.isEmpty()) {
      s.setRightAddressRanges(new HashSet<AddressRange>(rar));
    }

    return s;
  }

  /**
   * Computes a metric length on Earth of the given line in meters using
   * {@link DistanceOnEarth}.
   * 
   * @param line the line to compute length of (<code>null</code> not permitted)
   * @return a length of the lines in meters or 0 if the line contains zero or
   *         only one coordinate
   * @see DistanceOnEarth
   * @see LineStringLength#computeLengthMeters(LineString)
   */
  public int computeLengthMeters(LineString line) {
    return LineStringLength.computeLengthMeters(line);
  }
}
