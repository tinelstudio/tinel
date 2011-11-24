/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.commons.DistanceOnEarth;
import net.tinelstudio.gis.client.control.AbstractMapControl;
import net.tinelstudio.gis.client.control.image.layer.ImageMapLayerControl;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.renderer.lite.RendererUtilities;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author TineL
 */
public class ImageMapControlImpl extends AbstractMapControl implements
  ImageMapControl {

  private MapContext mapContext;

  private Dimension currentSize;

  private Envelope currentBounds;

  private final List<ImageMapLayerControl> layerControls=new ArrayList<ImageMapLayerControl>();

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    this.mapContext=new DefaultMapContext(getTargetCrs());
  }

  @Override
  public void destroy() throws Exception {
    for (ImageMapLayerControl layerControl : getLayerControls()) {
      layerControl.destroy();
    }
  }

  @Override
  public void addLayer(ImageMapLayerControl layer) {
    getLayerControls().add(layer);
    getMapContext().addLayer(layer.getLayer());
  }

  @Override
  public void insertLayer(ImageMapLayerControl layer, int index) {
    getLayerControls().add(index, layer);
    getMapContext().addLayer(index, layer.getLayer());
  }

  @Override
  public void removeLayer(ImageMapLayerControl layer) {
    getLayerControls().remove(layer);
    getMapContext().removeLayer(layer.getLayer());
  }

  @Override
  public void prepare() {
    try {
      Envelope bounds=computeEnvelope();

      setBounds(bounds);
      this.currentBounds=bounds;
      this.currentSize=getSize();

    } catch (Exception e) {
      logger.error("", e);
      return;
    }

    for (ImageMapLayerControl layerControl : getLayerControls()) {
      layerControl.prepare(this);
    }
  }

  @Override
  public void setTargetCrs(CoordinateReferenceSystem targetCrs)
    throws Exception {
    super.setTargetCrs(targetCrs);

    // Dynamically change
    MapContext mapContext=getMapContext();
    if (mapContext!=null) {
      mapContext.setCoordinateReferenceSystem(getTargetCrs());
    }
  }

  private Envelope computeEnvelope() throws Exception {
    Dimension size=getSize();
    int width=size.width;
    int height=size.height;

    Assert.isTrue(width>0&&height>0, "Map width & height must be > 0");

    // protected void computeEnvelope(double x, double y, int width, int height)
    // {
    // protected void computeEnvelope(DirectPosition c2, int width, int height)
    // {
    // Coordinate c=new Coordinate(c2.getx, c2.y);
    // Envelope e=EquirectangularEnvelope.toEnvelope(c, width,
    // height,getScaleRatio());
    //       
    // CoordinateReferenceSystem
    // worldCrs=WmsEquirectangularProjectedCrs.DEFAULT;
    // CoordinateReferenceSystem dataCrs=GoogleMapsProjectedCrs.DEFAULT;
    // ReferencedEnvelope envelope = new ReferencedEnvelope(e, worldCrs);
    // // Sample 10 points around the envelope
    // e= envelope.transform(dataCrs, true, 10 );

    // Envelope e=AutoOrthographicEnvelope.toEnvelope(c, width,
    // height,getScaleRatio());
    // Envelope e=UtmEnvelope.toEnvelope(c, width, height,getScaleRatio());
    // Envelope e=GoogleMapsEnvelope.toEnvelope(c, width,
    // height,getScaleRatio());

    // Ellipsoidal axis units
    Envelope e=toEnvelope(getCenter(), width, height, getScaleRatio());

    // Real meters
    // Envelope e=DistanceOnGlobeEnvelope.toEnvelope(getCenter(), width, height,
    // getScaleRatio(), this);

    // Envelope e=GoogleMaps2Envelope.toEnvelope(c, width,
    // height,getScaleRatio());
    // Envelope e=GeodeticCalcEnvelope.toEnvelope(c, width,
    // height,getScaleRatio());
    // Envelope e=DistanceOnGlobeEnvelope.toEnvelope(c, width, height,

    // CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
    // // CoordinateReferenceSystem sourceCRS2 = DefaultGeographicCRS.WGS84;
    //       
    // // System.out.println(sourceCRS.toWKT());
    // // System.out.println(sourceCRS2.toWKT());
    //       
    // CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
    // CoordinateReferenceSystem targetCRS2 = GoogleMapsProjectedCrs.DEFAULT;
    //
    // ReferencedEnvelope envelope = new ReferencedEnvelope(14, 16, 43, 89,
    // sourceCRS);
    // // MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
    //
    // // Sample 10 points around the envelope
    // ReferencedEnvelope result = envelope.transform( targetCRS, true, 10 );
    // ReferencedEnvelope result2 = envelope.transform( targetCRS2, true, 10 );
    //
    // System.out.println(envelope);
    // System.out.println("Wanted center="+getCenter());
    // System.out.println("for map      ="+width+"x"+height);
    // System.out.println("Got envelope ="+e);
    // System.out.println("with center  ="+e.centre());
    // System.out.println(result);
    // System.out.println(result2);

    // DirectPosition center=new GeneralDirectPosition(0, 0);
    // DirectPosition centerMeters=new GeneralDirectPosition(e.centre().x, e
    // .centre().y);
    // DirectPosition
    // center=getMathTransform().inverse().transform(centerMeters,
    // null);
    // System.out.println("with center  ="+center);

    // setAreaOfInterest(e);
    // setAreaOfInterest(new ReferencedEnvelope(e.getMinX(), e
    // .getMaxX(), e.getMinY(), e.getMaxY(),
    // DefaultGeographicCRS.WGS84));

    return e;
  }

  // 1 pixel = pixelMeterRatio meters
  private Envelope toEnvelope(Coordinate c, int width, int height,
    double pixelMeterRatio) throws Exception {
    double x=c.x;
    DistanceOnEarth.normalizeLongitude(x);
    DirectPosition center=new GeneralDirectPosition(x, c.y);
    // DirectPosition center=new GeneralDirectPosition(0, 0);

    /*
     * FIXME These are not real meters! The real unit is the one that is used by
     * the projection. In case of the Trans Mercator 46° North (EPSG:32633), the
     * units are real meters, but valid only in the 46° area. In case of the
     * Equirectangular projection, the units are 1:1 against latitude and
     * longitude, and can never be real meters. Something like: distance - The
     * orthodromic distance in the same units as the ellipsoid axis.
     */

    DirectPosition centerMeters=getMathTransform().transform(center, null);

    // With projection scale fix
    // double scale=1d/Math.cos(Math.toRadians(c.y));
    double scale=1.0;
    double xOffset=width/2d*pixelMeterRatio*scale;
    double yOffset=height/2d*pixelMeterRatio*scale;
    // double xOffset=15000000;
    // double yOffset=15000000/width*height;

    // // double sc=1d/width/height;
    // // double sc=height/width;
    // double sc=0;
    //  
    // double cos= Math.cos(Math.toRadians(c.y));
    // System.out.println("cos="+ cos+" 1/cos="+1d/cos);
    //    
    // double mer=2.0*Math.log((1.0+Math.sin(c.y))/(1.0-Math.sin(c.y)));
    // System.out.println("mer="+ mer+" 1/mer="+1d/mer);
    //   
    // double ex=23.2;
    // double a=1;
    // // double down= (ex+ex*sc)*a;
    // // double up=(ex+ex*sc)/a;
    // double down=ex*a;
    // double up=ex/a;

    // DirectPosition tlPixels=new GeneralDirectPosition(centerMeters
    // .getOrdinate(0)
    // -xOffset, centerMeters.getOrdinate(1)+yOffset);
    // DirectPosition brPixels=new GeneralDirectPosition(centerMeters
    // .getOrdinate(0)
    // +xOffset, centerMeters.getOrdinate(1)-yOffset);

    // DirectPosition tl=getInverseMathTransform().transform(tlPixels, null);
    // DirectPosition br=getInverseMathTransform().transform(brPixels, null);

    // tl=new GeneralDirectPosition(-180,85);
    // br=new GeneralDirectPosition(180,-85);
    // tl=new GeneralDirectPosition(c.x-ex,c.y+up);
    // br=new GeneralDirectPosition(c.x+ex,c.y-down);

    // ReferencedEnvelope e=mapContext.getLayerBounds();
    // Envelope e=new Envelope(tl.getOrdinate(0), br.getOrdinate(0),
    // tl.getOrdinate(1), br.getOrdinate(1));

    // Envelope e=new Envelope(tlPixels.getOrdinate(0), brPixels.getOrdinate(0),
    // tlPixels.getOrdinate(1), brPixels.getOrdinate(1));

    Envelope e=new Envelope(centerMeters.getOrdinate(0)-xOffset, centerMeters
      .getOrdinate(0)
      +xOffset, centerMeters.getOrdinate(1)-yOffset, centerMeters
      .getOrdinate(1)
      +yOffset);

    // ReferencedEnvelope re=new ReferencedEnvelope(e, targetCrs);
    // ReferencedEnvelope tre=re.transform(targetCrs, true);

    Rectangle dr=new Rectangle(width, height);
    return fixAspectRatio(dr, e);
    // return e;

    // return new Envelope(tl.getOrdinate(0), br.getOrdinate(0),
    // tl.getOrdinate(1), br.getOrdinate(1));
  }

  // Copied from GeoTools
  private Envelope fixAspectRatio(Rectangle r, Envelope mapArea) {
    double mapWidth=mapArea.getWidth(); /* get the extent of the map */
    double mapHeight=mapArea.getHeight();
    double scaleX=r.getWidth()/mapArea.getWidth(); /*
                                                    * calculate the new scale
                                                    */

    double scaleY=r.getHeight()/mapArea.getHeight();
    double scale=1.0; // stupid compiler!

    if (scaleX<scaleY) { /* pick the smaller scale */
      scale=scaleX;
    } else {
      scale=scaleY;
    }

    /* calculate the difference in width and height of the new extent */
    double deltaX= /* Math.abs */((r.getWidth()/scale)-mapWidth);
    double deltaY= /* Math.abs */((r.getHeight()/scale)-mapHeight);

    /*
     * System.out.println("delta x " + deltaX); System.out.println("delta y " +
     * deltaY);
     */

    /* create the new extent */
    Coordinate ll=new Coordinate(mapArea.getMinX()-(deltaX/2.0), mapArea
      .getMinY()
      -(deltaY/2.0));
    Coordinate ur=new Coordinate(mapArea.getMaxX()+(deltaX/2.0), mapArea
      .getMaxY()
      +(deltaY/2.0));

    return new Envelope(ll, ur);
  }

  // ##########################################################

  @Override
  public ReferencedEnvelope projToWorld(Envelope e) {
    CoordinateReferenceSystem worldCrs=getSourceCrs();
    CoordinateReferenceSystem projCrs=getTargetCrs();
    ReferencedEnvelope gree=new ReferencedEnvelope(e, projCrs);
    ReferencedEnvelope mapArea=null;
    try {
      mapArea=gree.transform(worldCrs, true);

    } catch (Exception ex) {
      logger.error("", ex);
    }
    return mapArea;
  }

  @Override
  public ReferencedEnvelope worldToProj(Envelope e) {
    CoordinateReferenceSystem worldCrs=getSourceCrs();
    CoordinateReferenceSystem projCrs=getTargetCrs();
    ReferencedEnvelope gree=new ReferencedEnvelope(e, worldCrs);
    ReferencedEnvelope mapArea=null;
    try {
      mapArea=gree.transform(projCrs, true);

    } catch (Exception ex) {
      logger.error("", ex);
    }
    return mapArea;
  }

  @Override
  public Coordinate worldToProj(double longitude, double latitude) {
    if (getMathTransform()==null) return null;

    // Degrees to meters
    GeneralDirectPosition dp=new GeneralDirectPosition(longitude, latitude);
    DirectPosition pos;
    try {
      pos=getMathTransform().transform(dp, null);

    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
    return new Coordinate(pos.getOrdinate(0), pos.getOrdinate(1));
  }

  @Override
  public Coordinate projToWorld(double longitude, double latitude) {
    if (getMathTransform()==null) return null;

    // Meters to degrees
    GeneralDirectPosition meters=new GeneralDirectPosition(longitude, latitude);
    DirectPosition pos;
    try {
      pos=getMathTransform().inverse().transform(meters, null);

    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
    return new Coordinate(pos.getOrdinate(0), pos.getOrdinate(1));
  }

  @Override
  public Point projToScreen(double longitude, double latitude) {
    if (getCurrentBounds()==null) return null;

    int px;
    int py;
    try {
      // Meters to pixels
      AffineTransform transform=RendererUtilities.worldToScreenTransform(
        getCurrentBounds(), new Rectangle(getCurrentSize()), getTargetCrs());

      Point2D src=new Point2D.Double(longitude, latitude);
      Point2D p=transform.transform(src, null);

      px=(int)Math.round(p.getX());
      py=(int)Math.round(p.getY());

    } catch (Exception ex) {
      logger.error("", ex);
      return null;
    }
    return new Point(px, py);
  }

  @Override
  public Coordinate screenToProj(int x, int y) {
    if (getCurrentBounds()==null) return null;

    Point2D p;
    try {
      // Meters to pixels
      AffineTransform transform=RendererUtilities.worldToScreenTransform(
        getCurrentBounds(), new Rectangle(getCurrentSize()), getTargetCrs());

      Point2D src=new Point2D.Double(x, y);
      p=transform.inverseTransform(src, null);

    } catch (Exception ex) {
      logger.error("", ex);
      return null;
    }
    return new Coordinate(p.getX(), p.getY());
  }

  @Override
  public Coordinate screenToWorld(int x, int y) {
    Coordinate pos=screenToProj(x, y);
    if (pos==null) return null;
    return projToWorld(pos);
  }

  @Override
  public Point worldToScreen(double longitude, double latitude) {
    Coordinate pos=worldToProj(longitude, latitude);
    if (pos==null) return null;
    return projToScreen(pos);
  }

  private List<ImageMapLayerControl> getLayerControls() {
    return this.layerControls;
  }

  @Override
  public MapContext getMapContext() {
    return this.mapContext;
  }

  /**
   * @return current map bounds in target CRS
   */
  private Envelope getCurrentBounds() {
    return this.currentBounds;
  }

  private Dimension getCurrentSize() {
    return this.currentSize;
  }
}
