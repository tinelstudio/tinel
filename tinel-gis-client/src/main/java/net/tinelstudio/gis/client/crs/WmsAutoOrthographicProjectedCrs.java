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
 * Auto orthographic Layer CRS (AUTO2:42003) defined by the OpenGIS WMS 1.3.0
 * Implementation Specification document number 06-042, section B.9, page 49.
 * 
 * @author TineL
 */
public class WmsAutoOrthographicProjectedCrs implements CrsFactory {

  private final Log logger=LogFactory.getLog(getClass());

  private CoordinateReferenceSystem crs;

  /**
   * The variables "latitude" and "longitude" are the central point of the
   * projection appearing in the CRS parameter of the map request. The
   * coordinate operation method uses spherical formulas.
   * 
   * <pre>
   * latitudeOfOrigin = latitude
   * centralMeridian = longitude
   * </pre>
   * 
   * @param latitudeOfOrigin the latitude of origin value
   * @param centralMeridian the central meridian value
   */
  public WmsAutoOrthographicProjectedCrs(double latitudeOfOrigin,
    double centralMeridian) {
    try {

      this.crs=CRS.parseWKT("PROJCS[\"WGS 84 / Auto Orthographic\","
        +"GEOGCS[\"WGS 84\","
        +"DATUM[\"WGS_1984\","
        +"SPHEROID[\"WGS_1984\", 6378137, 298.257223563]],"
        +"PRIMEM[\"Greenwich\", 0],"
        +"UNIT[\"Decimal_Degree\", 0.0174532925199433]],"
        +"PROJECTION[\"Orthographic\"],"
        +"PARAMETER[\"semi_major\", 6378137.0],"
        +"PARAMETER[\"semi_minor\", 6378137.0]," // Modify datum to be spherical
        +"PARAMETER[\"Latitude_of_Origin\", "+latitudeOfOrigin+"],"
        +"PARAMETER[\"Central_Meridian\", "+centralMeridian+"],"
        +"UNIT[\"Meter\", 1]]");

    } catch (FactoryException e) {
      logger.error("Internal error", e);
    }
  }

  @Override
  public CoordinateReferenceSystem getCrs() {
    return this.crs;
  }
}
