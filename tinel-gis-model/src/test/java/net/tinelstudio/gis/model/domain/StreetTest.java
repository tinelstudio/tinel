/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
import java.util.Set;

import net.tinelstudio.gis.common.dto.StreetDto.Level;

import org.junit.Test;

import com.vividsolutions.jts.geom.LineString;

/**
 * @author TineL
 */
public class StreetTest extends NotedEntityTest {

  @Override
  protected Street createInstance() {
    return new Street();
  }

  @Test
  public void testSetLineString() {
    LineString line=createMock(LineString.class);
    Street street=createInstance();
    street.setLineString(line);
    assertSame(line, street.getLineString());
  }

  @Test
  public void testSetGeoNames() {
    Set<GeoName> geoNames=new HashSet<GeoName>();
    Street street=createInstance();
    street.setGeoNames(geoNames);
    assertSame(geoNames, street.getGeoNames());
  }

  @Test
  public void testSetLeftAddressRanges() {
    Set<AddressRange> addressRanges=new HashSet<AddressRange>();
    Street street=createInstance();
    street.setLeftAddressRanges(addressRanges);
    assertSame(addressRanges, street.getLeftAddressRanges());
  }

  @Test
  public void testSetRightAddressRanges() {
    Set<AddressRange> addressRanges=new HashSet<AddressRange>();
    Street street=createInstance();
    street.setRightAddressRanges(addressRanges);
    assertSame(addressRanges, street.getRightAddressRanges());
  }

  @Test
  public void testSetLengthMeters() {
    Integer lengthMeters=56;
    Street street=createInstance();
    street.setLengthMeters(lengthMeters);
    assertSame(lengthMeters, street.getLengthMeters());
  }

  @Test
  public void testSetLevel() {
    Street street=createInstance();
    Level lengthMeters=Level.MEDIUM;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.MAJOR;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.MINOR;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
    lengthMeters=Level.UNKNOWN;
    street.setLevel(lengthMeters);
    assertSame(lengthMeters, street.getLevel());
  }

  @Test
  public void testSetOneWay() {
    Boolean oneWay=Boolean.FALSE;
    Street street=createInstance();
    street.setOneWay(oneWay);
    assertSame(oneWay, street.getOneWay());
  }

  @Test
  public void testSetStartNode() {
    StreetNode streetNode=createMock(StreetNode.class);
    Street street=createInstance();
    street.setStartNode(streetNode);
    assertSame(streetNode, street.getStartNode());
  }

  @Test
  public void testSetEndNode() {
    StreetNode streetNode=createMock(StreetNode.class);
    Street street=createInstance();
    street.setEndNode(streetNode);
    assertSame(streetNode, street.getEndNode());
  }
}
