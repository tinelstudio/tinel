/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author TineL
 */
public abstract class AbstractMapControl implements MapControl {

  protected final Log logger=LogFactory.getLog(getClass());

  private Envelope mapEnvelope; // In meters!

  private Coordinate center;

  private int scale;

  private Dimension size=new Dimension(0, 0);

  private CoordinateReferenceSystem sourceCrs=DefaultGeographicCRS.WGS84;
  private CoordinateReferenceSystem targetCrs;

  private MathTransform wgs2proj;
  private MathTransform proj2wgs;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.targetCrs, "Property 'targetCrs' not set");
  }

  protected MathTransform getMathTransform() {
    return this.wgs2proj;
  }

  protected MathTransform getInverseMathTransform() {
    return this.proj2wgs;
  }

  protected CoordinateReferenceSystem getSourceCrs() {
    return this.sourceCrs;
  }

  public CoordinateReferenceSystem getTargetCrs() {
    return this.targetCrs;
  }

  public void setTargetCrs(CoordinateReferenceSystem targetCrs)
    throws Exception {
    this.wgs2proj=CRS.findMathTransform(sourceCrs, targetCrs);
    this.proj2wgs=this.wgs2proj.inverse();
    this.targetCrs=targetCrs;
  }

  protected double getScaleRatio() {
    return getScale()*getPixelMeterRatio();
  }

  protected double getPixelMeterRatio() {
    int inchPixelRatio=Toolkit.getDefaultToolkit().getScreenResolution();
    double inchMeterRatio=0.0254;
    double pixelMeterRatio=1d/inchPixelRatio*inchMeterRatio;
    return pixelMeterRatio;
  }

  @Override
  public Envelope getBounds() {
    return this.mapEnvelope;
  }

  @Override
  public Coordinate getCenter() {
    return this.center;
  }

  @Override
  public void setCenter(Coordinate center) {
    this.center=center;
  }

  @Override
  public void setCenter(double longitude, double latitude) {
    setCenter(new Coordinate(longitude, latitude));
  }

  @Override
  public int getScale() {
    return this.scale;
  }

  @Override
  public void setScale(int scale) {
    this.scale=scale;
  }

  @Override
  public Dimension getSize() {
    return this.size;
  }

  @Override
  public void setSize(Dimension size) {
    this.size=size;
  }

  @Override
  public void setSize(int width, int height) {
    setSize(new Dimension(width, height));
  }

  @Override
  public void setBounds(Envelope bounds) {
    this.mapEnvelope=bounds;
  }

  @Override
  public void setBounds(Coordinate coordinate1, Coordinate coordinate2) {
    setBounds(new Envelope(coordinate1, coordinate2));
  }

  @Override
  public void setBounds(double longitude1, double latitude1, double longitude2,
    double latitude2) {
    setBounds(new Envelope(longitude1, longitude2, latitude1, latitude2));
  }

  @Override
  public Coordinate worldToProj(Coordinate c) {
    return worldToProj(c.x, c.y);
  }

  @Override
  public Coordinate projToWorld(Coordinate c) {
    return projToWorld(c.x, c.y);
  }

  @Override
  public Point projToScreen(Coordinate c) {
    return projToScreen(c.x, c.y);
  }

  @Override
  public Coordinate screenToProj(Point point) {
    return screenToProj(point.x, point.y);
  }

  @Override
  public Coordinate screenToWorld(Point point) {
    return screenToWorld(point.x, point.y);
  }

  @Override
  public Point worldToScreen(Coordinate coordinate) {
    return worldToScreen(coordinate.x, coordinate.y);
  }
}
