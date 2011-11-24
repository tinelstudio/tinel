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
public class WmsEquidistantCylindricalProjectedCrs implements CrsFactory {

  private final Log logger=LogFactory.getLog(getClass());

  private CoordinateReferenceSystem crs;

  /**
   * @param standardParallel the latitude of center view point
   */
  public WmsEquidistantCylindricalProjectedCrs(double standardParallel) {
    try {

      this.crs=CRS
        .parseWKT("PROJCS[\"WGS 84 / WMS Equidistant Cylindrical\","
          +"GEOGCS[\"WGS 84\","
          +"DATUM[\"WGS_1984\","
          +"SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],"
          +"AUTHORITY[\"EPSG\",\"6326\"]],"
          +"PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],"
          +"UNIT[\"degree\", 0.017453292519943295],"
          +"AXIS[\"Longitude\", EAST],"+"AXIS[\"Latitude\", NORTH],"
          +"AUTHORITY[\"EPSG\",\"4326\"]],"
          +"PROJECTION[\"Equidistant Cylindrical\"],"
          /*
           * TODO There is obviously a bug in GeoTools not reading the
           * 'standard_parallel_1' parameter in
           * org.geotools.referencing.operation
           * .projection.EquidistantCylindrical map projection. So this CRS acts
           * the same as WmsEquirectangularProjectedCrs, the map projection
           * equals to
           * org.geotools.referencing.operation.projection.PlateCarree.
           */
          +"PARAMETER[\"standard_parallel_1\", "+standardParallel+"],"
          +"PARAMETER[\"Longitude of false origin\", 0.0],"
          +"PARAMETER[\"false_easting\", 0.0],"
          +"PARAMETER[\"false_northing\", 0.0],"
          +"PARAMETER[\"semi_minor\", 6378137.0]," // Modify to be spherical
          +"UNIT[\"m\", 1.0],"+"AXIS[\"x\", EAST],"+"AXIS[\"y\", NORTH]]");

    } catch (FactoryException e) {
      logger.error("Internal error", e);
    }
  }

  @Override
  public CoordinateReferenceSystem getCrs() {
    return this.crs;
  }
}
