/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author TineL
 */
public abstract class ConverterSupport {

  private GeometryFactory geometryFactory;

  private WKTReader wktReader;

  /**
   * Deserializes the point by creating new point.
   * 
   * @param point the point to deserialize (<code>null</code> permitted)
   * @return a deserialized point (<code>null</code> possible)
   */
  public Point deserializePoint(Point point) {
    if (point==null) return null;
    Point desePoint=getGeometryFactory().createPoint(
      new Coordinate(point.getCoordinate()));
    desePoint.setSRID(point.getSRID());
    return desePoint;
  }

  /**
   * Deserializes the line string by creating new line string.
   * 
   * @param lineString the line string to deserialize (<code>null</code>
   *        permitted)
   * @return a deserialized line string (<code>null</code> possible)
   */
  public LineString deserializeLineString(LineString lineString) {
    if (lineString==null) return null;
    CoordinateArraySequence cas=new CoordinateArraySequence(lineString
      .getCoordinateSequence());
    LineString newLine=getGeometryFactory().createLineString(cas);
    newLine.setSRID(lineString.getSRID());
    return newLine;
  }

  /**
   * Deserializes the polygon by creating new polygon using {@link WKTReader}.
   * 
   * @param polygon the polygon to deserialize (<code>null</code> permitted)
   * @return a deserialized polygon (<code>null</code> possible)
   */
  public Polygon deserializePolygon(Polygon polygon) {
    if (polygon==null) return null;
    Polygon desePolygon;
    try {
      desePolygon=(Polygon)getWktReader().read(polygon.toText());
      desePolygon.setSRID(polygon.getSRID());

    } catch (ParseException e) {
      throw new InternalError("WKTReader ParseException: Cannot read polygon");
    }
    return desePolygon;
  }

  public GeometryFactory getGeometryFactory() {
    if (this.geometryFactory==null) {
      this.geometryFactory=new GeometryFactory();
    }
    return this.geometryFactory;
  }

  public void setGeometryFactory(GeometryFactory geometryFactory) {
    this.geometryFactory=geometryFactory;
  }

  public WKTReader getWktReader() {
    if (this.wktReader==null) {
      this.wktReader=new WKTReader(getGeometryFactory());
    }
    return this.wktReader;
  }

  public void setWktReader(WKTReader wktReader) {
    this.wktReader=wktReader;
  }
}
