/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.crs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author TineL
 */
public class GoogleMapsProjectedCrs implements CrsFactory {

  private final Log logger=LogFactory.getLog(getClass());

  private CoordinateReferenceSystem crs;

  public GoogleMapsProjectedCrs() {
    try {

      // From http://spatialreference.org/ref/sr-org/6627/html/
      this.crs=CRS
        .parseWKT("PROJCS[\"Google Mercator\","
          +"GEOGCS[\"WGS 84\","
          +"DATUM[\"World Geodetic System 1984\","
          +"SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
          +"AUTHORITY[\"EPSG\",\"6326\"]],"
          +"PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
          +"UNIT[\"degree\", 0.017453292519943295],"
          +"AXIS[\"Geodetic latitude\", NORTH],"
          +"AXIS[\"Geodetic longitude\", EAST],"
          +"AUTHORITY[\"EPSG\",\"4326\"]],"
          +"PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]],"
          +"PARAMETER[\"semi_major\", 6378137.0],"
          +"PARAMETER[\"semi_minor\", 6378137.0],"
          +"PARAMETER[\"latitude_of_origin\", 0.0],"
          +"PARAMETER[\"central_meridian\", 0.0],"
          +"PARAMETER[\"scale_factor\", 1.0],"
          +"PARAMETER[\"false_easting\", 0.0],"
          +"PARAMETER[\"false_northing\", 0.0],"+"UNIT[\"m\", 1.0],"
          +"AXIS[\"Easting\", EAST],"+"AXIS[\"Northing\", NORTH],"
          +"AUTHORITY[\"EPSG\",\"900913\"]]");

    } catch (FactoryException e) {
      logger.error("Internal error", e);
    }
  }

  @Override
  public CoordinateReferenceSystem getCrs() {
    return this.crs;
  }
}
