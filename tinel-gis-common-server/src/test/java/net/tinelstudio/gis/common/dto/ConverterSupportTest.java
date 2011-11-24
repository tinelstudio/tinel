/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author TineL
 */
public class ConverterSupportTest {

  protected ConverterSupport createInstance() {
    return new ConverterSupport() {};
  }

  @Test
  public void testDeserializeNullPoint() {
    ConverterSupport cs=createInstance();
    assertNull(cs.deserializePoint(null));
  }

  @Test
  public void testDeserializePoint() {
    // Init
    int srid=4326;
    GeometryFactory gf=new GeometryFactory(new PrecisionModel(), srid);
    Coordinate c=new Coordinate(15, 46);
    Point point=gf.createPoint(c);

    // Run
    ConverterSupport cs=createInstance();
    Point desePoint=cs.deserializePoint(point);

    // Verify
    assertTrue(point.equalsExact(desePoint));
    assertEquals(srid, desePoint.getSRID());

    // Serialization test: Must not be same instances
    assertNotSame(point.getCoordinate(), desePoint.getCoordinate());
    assertNotSame(point.getCoordinateSequence(), desePoint
      .getCoordinateSequence());
    assertNotSame(point.getFactory(), desePoint.getFactory());
  }

  @Test
  public void testDeserializeNullLineString() {
    ConverterSupport cs=createInstance();
    assertNull(cs.deserializeLineString(null));
  }

  @Test
  public void testDeserializeLineString() {
    // Init
    int srid=4326;
    GeometryFactory gf=new GeometryFactory(new PrecisionModel(), srid);
    Coordinate c1=new Coordinate(15.5, 46.2);
    Coordinate c2=new Coordinate(15.6, 46.3);
    Coordinate c3=new Coordinate(15.6, 46.4);
    Coordinate[] cs=new Coordinate[]{c1, c2, c3};
    LineString line=gf.createLineString(cs);

    // Run
    ConverterSupport converter=createInstance();
    LineString deseLine=converter.deserializeLineString(line);

    // Verify
    assertTrue(line.equalsExact(deseLine));
    assertEquals(srid, deseLine.getSRID());

    // Serialization test: Must not be same instances
    assertNotSame(cs, deseLine.getCoordinates());
    assertNotSame(line.getCoordinateSequence(), deseLine
      .getCoordinateSequence());
    assertNotSame(line.getFactory(), deseLine.getFactory());

  }

  @Test
  public void testDeserializeNullPolygon() {
    ConverterSupport cs=createInstance();
    assertNull(cs.deserializePolygon(null));
  }

  @Test
  public void testDeserializePolygon() {
    // Init
    int srid=4326;
    GeometryFactory gf=new GeometryFactory(new PrecisionModel(), srid);
    Coordinate c1=new Coordinate(15.5, 46.2);
    Coordinate c2=new Coordinate(15.6, 46.3);
    Coordinate c3=new Coordinate(15.6, 46.4);
    Coordinate c4=new Coordinate(15.6, 46.3);
    Coordinate c5=new Coordinate(15.5, 46.2);
    Coordinate[] cs=new Coordinate[]{c1, c2, c3, c4, c5};
    Coordinate h11=new Coordinate(15.51, 46.31);
    Coordinate h12=new Coordinate(15.52, 46.32);
    Coordinate h13=new Coordinate(15.52, 46.33);
    Coordinate h14=new Coordinate(15.51, 46.31);
    Coordinate[] hcs1=new Coordinate[]{h11, h12, h13, h14};
    Coordinate h21=new Coordinate(15.54, 46.22);
    Coordinate h22=new Coordinate(15.56, 46.32);
    Coordinate h23=new Coordinate(15.57, 46.35);
    Coordinate h24=new Coordinate(15.54, 46.22);
    Coordinate[] hcs2=new Coordinate[]{h21, h22, h23, h24};
    LinearRing ring=gf.createLinearRing(cs);
    LinearRing hole1=gf.createLinearRing(hcs1);
    LinearRing hole2=gf.createLinearRing(hcs2);
    LinearRing[] holes=new LinearRing[]{hole1, hole2};
    Polygon polygon=gf.createPolygon(ring, holes);

    // Run
    ConverterSupport converter=createInstance();
    Polygon desePolygon=converter.deserializePolygon(polygon);

    // Verify
    assertTrue(polygon.equalsExact(desePolygon));
    assertEquals(srid, desePolygon.getSRID());

    // Serialization test: Must not be same instances
    assertNotSame(cs, desePolygon.getExteriorRing().getCoordinates());
    assertNotSame(ring.getCoordinates(), desePolygon.getExteriorRing()
      .getCoordinates());
    assertNotSame(hole1.getCoordinates(), desePolygon.getInteriorRingN(0)
      .getCoordinates());
    assertNotSame(hole2.getCoordinates(), desePolygon.getInteriorRingN(1)
      .getCoordinates());
    assertNotSame(polygon.getFactory(), desePolygon.getFactory());
  }

  @Test
  public void testGetGeometryFactory() {
    ConverterSupport cs=createInstance();
    assertNotNull(cs.getGeometryFactory());
  }

  @Test
  public void testSetGeometryFactory() {
    GeometryFactory gf=createMock(GeometryFactory.class);
    ConverterSupport cs=createInstance();
    cs.setGeometryFactory(gf);

    assertSame(gf, cs.getGeometryFactory());
  }

  @Test
  public void testGetWktReader() {
    ConverterSupport cs=createInstance();
    assertNotNull(cs.getWktReader());
  }

  @Test
  public void testSetWktReader() {
    WKTReader wktReader=createMock(WKTReader.class);
    ConverterSupport cs=createInstance();
    cs.setWktReader(wktReader);

    assertSame(wktReader, cs.getWktReader());
  }
}
