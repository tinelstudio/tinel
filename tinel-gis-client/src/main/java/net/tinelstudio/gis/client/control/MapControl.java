/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control;

import java.awt.Dimension;
import java.awt.Point;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author TineL
 */
public interface MapControl extends InitializingBean, DisposableBean {

  Coordinate getCenter();

  void setCenter(Coordinate center);

  void setCenter(double longitude, double latitude);

  int getScale();

  void setScale(int scale);

  /**
   * @return map envelope in target CRS
   */
  Envelope getBounds();

  /**
   * @param bounds map envelope in target CRS
   */
  void setBounds(Envelope bounds);

  void setBounds(Coordinate coordinate1, Coordinate coordinate2);

  void setBounds(double longitude1, double latitude1, double longitude2,
    double latitude2);

  Dimension getSize();

  void setSize(Dimension size);

  void setSize(int width, int height);

  /**
   * Prepares map for rendering.
   */
  void prepare();

  // #############################################################

  ReferencedEnvelope projToWorld(Envelope e);

  ReferencedEnvelope worldToProj(Envelope e);

  Coordinate worldToProj(Coordinate c);

  Coordinate worldToProj(double longitude, double latitude);

  Coordinate projToWorld(Coordinate c);

  Coordinate projToWorld(double longitude, double latitude);

  Point projToScreen(Coordinate c);

  Point projToScreen(double longitude, double latitude);

  Coordinate screenToProj(int x, int y);

  Coordinate screenToProj(Point point);

  Coordinate screenToWorld(Point point);

  Coordinate screenToWorld(int x, int y);

  Point worldToScreen(Coordinate coordinate);

  Point worldToScreen(double longitude, double latitude);
}
