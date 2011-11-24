/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.crs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author TineL
 */
public class SimpleMercatorProjectedCrs implements CrsFactory {

  private final Log logger=LogFactory.getLog(getClass());

  private CoordinateReferenceSystem crs;

  public SimpleMercatorProjectedCrs() {
    try {

      this.crs=CRS.decode("EPSG:41001");

    } catch (Exception e) {
      logger.error("Internal error", e);
    }
  }

  @Override
  public CoordinateReferenceSystem getCrs() {
    return this.crs;
  }
}
