/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import net.tinelstudio.gis.client.control.image.ImageMapControl;
import net.tinelstudio.gis.client.control.image.layer.support.MutableRasterMapLayer;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * @author TineL
 */
public class WmsMapLayerControl extends AbstractImageMapLayerControl {

  private GridCoverageFactory factory=new GridCoverageFactory();

  private WebMapServer wms;

  private CoordinateReferenceSystem dataCrs;

  // private MutableMapLayer wmsLayer;

  // private Properties mapProperties;

  private String imageFormat;
  private String host;
  private int port;
  private String srs="EPSG:4326"; // WGS84
  // private String srs="EPSG:41001"; // Simple Mercator
  // private String srs="EPSG:32633"; // UTM 33°N
  private final List<String> layers=new ArrayList<String>();
  private String namespace;

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    String mapName=getMap().getName();

    logger.info("  Connecting to '"+mapName+"'");

    // TODO Read from Properties

    this.host="localhost";
    this.port=1101;
    this.namespace="autr";
    double minZoom=0.004;
    double maxZoom=2500;
    this.imageFormat="image/png";
    logger.info("    server "+getHost()+":"+getPort());
    logger.info("    namespace "+namespace);
    logger.info("    EPSG "+srs);
    logger.info("    zoom limits "+minZoom+", "+maxZoom);
    logger.info("    image format "+imageFormat);

    // Prepare the WMS Capabilities document URL
    URL url=new URL("http://"+getHost()+":"+getPort()
      +"/geoserver/wms?version=1.1.1&request=getCapabilities");

    // Connect to GeoServer
    try {
      this.wms=new WebMapServer(url);

    } catch (IOException e) {
      // There was an error communicating with the server
      // For example, the server is down
      logger
        .error("Cannot reach WMS '"+mapName+"' at "+getHost()+":"+getPort());
      throw e;

    } catch (ServiceException e) {
      // The server returned a ServiceException (unusual in this case)
      logger.error("The WMS '"+mapName+"' returned a ServiceException");
      throw e;
    }

    // Get WMS capabilities
    WMSCapabilities capabilities=getWms().getCapabilities();

    // Get original layer names, filter namespace, sort
    Layer[] layers=WMSUtils.getNamedLayers(capabilities);
    for (int li=0; li<layers.length; ++li) {
      String name=layers[li].getName();
      if (!name.startsWith(namespace)) continue;
      getLayers().add(name);
    }
    Collections.sort(getLayers());

    logger.info("  Connected");

    this.dataCrs=CRS.decode(getSrs());

    // Create map layer
    MutableRasterMapLayer wmsLayer=new MutableRasterMapLayer(mapName);
    setLayer(wmsLayer);
  }

  @Override
  public void destroy() throws Exception {}

  @Override
  public void prepare(ImageMapControl mapControl) {
    try {
      GridCoverage2D gc=getMap(mapControl);
      getLayer().setGridCoverage(gc);

    } catch (Exception e) {
      logger.error("", e);
    }
  }

  private GridCoverage2D getMap(ImageMapControl mapControl) throws Exception {
    Dimension size=mapControl.getSize();
    int width=size.width;
    int height=size.height;

    Assert.isTrue(width>0&&height>0, "Map width & height must be > 0");

    // // Envelope env=getMapEnvelope();
    Envelope env0=mapControl.getBounds();
    // ReferencedEnvelope env=mapControl.getEnvelopeM();

    CoordinateReferenceSystem screenCrs=DefaultGeographicCRS.WGS84;
    // CoordinateReferenceSystem
    // screenCrs=WmsEquirectangularProjectedCrs.DEFAULT;
    CoordinateReferenceSystem projCrs=mapControl.getMapContext()
      .getCoordinateReferenceSystem();

    ReferencedEnvelope gree=new ReferencedEnvelope(env0, projCrs);
    ReferencedEnvelope env=gree.transform(screenCrs, true);

    // // Envelope env=getMapEnvelope();
    // Envelope env=mapControl.getAreaOfInterest();
    // // ReferencedEnvelope env=mapControl.getAreaOfInterest();
    Coordinate c=env.centre();

    logger.debug("imageAt=("+env.getMaxY()+","+env.getMinX()+" "+env.getMinY()
      +","+env.getMaxX()+")");
    logger.debug("center=("+c.x+","+c.y+")");
    logger.debug("imageSize=("+width+","+height+")");

    // Prepare WMS request
    GetMapRequest mapRequest=getWms().createGetMapRequest();
    mapRequest.setFormat(getImageFormat());
    mapRequest.setDimensions(width, height);
    mapRequest.setTransparent(true);
    mapRequest.setSRS(getSrs());
    double maxY=env.getMaxY();
    double minY=env.getMinY();
    double maxX=env.getMaxX();
    double minX=env.getMinX();
    CRSEnvelope crse=new CRSEnvelope(getSrs(), minX, minY, maxX, maxY);
    mapRequest.setBBox(crse);

    // Add visible layers
    for (String originalLayerName : getLayers()) {
      Layer layer=new Layer();
      layer.setName(originalLayerName);
      mapRequest.addLayer(layer);
    }

    // Get map from GeoServer
    GetMapResponse mapResponse;
    try {
      mapResponse=getWms().issueRequest(mapRequest);

    } catch (IOException e) {
      // There was an error communicating with the server
      // For example, the server is down
      logger.error("Cannot reach WMS '"+getMap().getName()+"' at "+getHost()
        +":"+getPort());
      throw e;

    } catch (ServiceException e) {
      // The server returned a ServiceException (unusual in this case)
      logger.error("The WMS '"+getMap().getName()
        +"' returned a ServiceException");
      throw e;
    }

    BufferedImage image;
    try {
      InputStream is=mapResponse.getInputStream();
      image=ImageIO.read(is);
      is.close();

    } catch (IOException e) {
      logger.error("Bad WMS '"+getMap().getName()+"' image at "+getHost()+":"
        +getPort());
      throw e;
    }

    logger.debug("Got image w="+image.getWidth()+", h="+image.getHeight());

    // ImageIO.write(image, "png", new File(getName()+".png"));

    // return new GridCoverageFactory().create(getName(), image,
    // // getAreaOfInterest());
    // // new ReferencedEnvelope(env, DefaultGeographicCRS.WGS84));
    // // new ReferencedEnvelope(env, WmsEquirectangularProjectedCrs.DEFAULT));
    // gree);

    ReferencedEnvelope envelope=env.transform(getDataCrs(), true);

    // ReferencedEnvelope envelope=new ReferencedEnvelope(env0, thisMapCrs);
    // ReferencedEnvelope envelope=new ReferencedEnvelope(env0, projCrs);
    // ReferencedEnvelope envelope=new ReferencedEnvelope(env0,
    // WmsAutoEquirectangularProjectedCrs.DEFAULT);
    // GridCoverageFactory factory=FactoryFinder.getGridCoverageFactory(null);
    GridCoverage2D coverage=getFactory().create(getMap().getName(), image,
      envelope);
    return coverage;
  }

  @Override
  public MutableRasterMapLayer getLayer() {
    return (MutableRasterMapLayer)super.getLayer();
  }

  private CoordinateReferenceSystem getDataCrs() {
    // DefaultGeographicCRS.WGS84;
    return dataCrs;
  }

  private String getImageFormat() {
    return this.imageFormat;
  }

  private String getSrs() {
    return this.srs;
  }

  private List<String> getLayers() {
    return this.layers;
  }

  private WebMapServer getWms() {
    return this.wms;
  }

  private GridCoverageFactory getFactory() {
    return this.factory;
  }

  private String getHost() {
    return this.host;
  }

  private int getPort() {
    return this.port;
  }
}