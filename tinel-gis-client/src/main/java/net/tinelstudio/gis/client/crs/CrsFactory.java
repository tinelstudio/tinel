/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.crs;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author TineL
 */
public interface CrsFactory {

  /**
   * @return the CRS (<code>null</code> not possible)
   */
  CoordinateReferenceSystem getCrs();
}
