/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import net.tinelstudio.commons.DistanceOnEarth;
import net.tinelstudio.gis.client.control.image.ImageMapControlImpl;
import net.tinelstudio.gis.client.control.image.layer.InMemoryMapLayerControl;
import net.tinelstudio.gis.client.control.image.layer.PostGisMapLayerControl;
import net.tinelstudio.gis.client.control.image.layer.ShapeMapLayerControl;
import net.tinelstudio.gis.client.control.image.layer.WmsMapLayerControl;
import net.tinelstudio.gis.client.control.image.layer.WorldImageMapLayerControl;
import net.tinelstudio.gis.client.crs.CrsFactory;
import net.tinelstudio.gis.client.crs.GoogleMapsProjectedCrs;
import net.tinelstudio.gis.client.crs.SimpleMercatorProjectedCrs;
import net.tinelstudio.gis.client.crs.WmsAutoOrthographicProjectedCrs;
import net.tinelstudio.gis.client.crs.WmsEquidistantCylindricalProjectedCrs;
import net.tinelstudio.gis.client.crs.WmsEquirectangularProjectedCrs;
import net.tinelstudio.gis.client.domain.DefaultMap;
import net.tinelstudio.gis.common.dto.AddressDto;
import net.tinelstudio.gis.common.dto.BuildingDto;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.common.dto.StreetDto;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.service.GeocodingService;
import net.tinelstudio.gis.reversegeocoding.service.ReverseGeocodingService;
import net.tinelstudio.gis.routing.dto.CustomRoutingParameters;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.service.RoutingService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * GIS GUI client for testing.
 * 
 * @author TineL
 */
public class ImageMapPanel extends JFrame {

  public static void main(String[] args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    new ImageMapPanel();
  }

  private final Log logger=LogFactory.getLog(getClass());

  private ImageMapControlImpl mapControl;

  private JPanel mapPanel;

  private JPopupMenu contextMenu;

  private JButton b1, b2, b3, b4;
  private JButton up, right, down, left;
  private JButton in, out;
  private JButton t1, t2, t3;
  private JMenuItem cm1, cm2, cm3;
  private JComboBox projectionCB;

  private JLabel coorsL;

  private StreamingRenderer renderer;

  private BufferedImage buffer;

  private int drawnCircleRadius=0;
  private double drawnCircleLng=0.0;
  private double drawnCircleLat=0.0;

  private int pressedX=0;
  private int pressedY=0;
  private int releasedX=0;
  private int releasedY=0;

  private PostGisMapLayerControl streetPostGisLayer;
  private PostGisMapLayerControl addressPostGisLayer;
  private PostGisMapLayerControl buildingPostGisLayer;
  private InMemoryMapLayerControl<com.vividsolutions.jts.geom.Point> pointMemoryLayer;
  private InMemoryMapLayerControl<LineString> lineMemoryLayer;
  private InMemoryMapLayerControl<Polygon> polygonMemoryLayer;

  private GeocodingDialog geocodingDialog;
  private ReverseGeocodingDialog reverseGeocodingDialog;
  private RoutingDialog routingDialog;

  private ReverseGeocodingService reverseGeocodingService;
  private GeocodingService geocodingService;
  private RoutingService routingService;

  public ImageMapPanel() {
    setTitle(getClass().getSimpleName());
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    try {
      mapControl=new ImageMapControlImpl();
      mapControl.setTargetCrs(new GoogleMapsProjectedCrs().getCrs());
      // mapControl.setTargetCrs(new SimpleMercatorProjectedCrs().getCrs());
      // mapControl.setTargetCrs(new WmsEquirectangularProjectedCrs().getCrs());
      // mapControl.setTargetCrs(new WmsAutoOrthographicProjectedCrs(0.0,
      // 0.0).getCrs());
      // mapControl.setTargetCrs(new
      // WmsEquirectangularProjectedCrs().getCrs());
      // mapControl.setTargetCrs(CRS.decode("epsg:32633"));

    } catch (Exception e) {
      e.printStackTrace();
    }

    setSize(840, 800);
    setLocationRelativeTo(null);
    add(buildComponents());
    setVisible(true);

    b1.doClick();
    b4.doClick();
  }

  protected void init() {
    /*
     * Create maps & layers
     */
    DefaultMap geoServer=new DefaultMap();
    geoServer.setName("GeoServer");
    WmsMapLayerControl geoServerLayer=new WmsMapLayerControl();
    geoServerLayer.setMap(geoServer);

    // String shapeSource="world_borders.shp";
    String shapeSource="../Tests1.6/world_borders.shp";
    DefaultMap worldShapeMap=new DefaultMap();
    worldShapeMap.setName("WorldShape");
    worldShapeMap.setSource(shapeSource);
    ShapeMapLayerControl worldShapeLayer=new ShapeMapLayerControl();
    worldShapeLayer.setMap(worldShapeMap);

    String streetPostGisSource="host=localhost,port=5432,database=tinel-gis,"
      +"schema=public,user=gis,password=gis,table=street,cache=true";
    DefaultMap streetPostGisMap=new DefaultMap();
    streetPostGisMap.setName("PostGIS Streets");
    streetPostGisMap.setSource(streetPostGisSource);
    streetPostGisLayer=new PostGisMapLayerControl();
    streetPostGisLayer.setMap(streetPostGisMap);

    String addressPostGisSource="host=localhost,port=5432,database=tinel-gis,"
      +"schema=public,user=gis,password=gis,table=address,cache=true";
    DefaultMap addressPostGisMap=new DefaultMap();
    addressPostGisMap.setName("PostGIS Addresses");
    addressPostGisMap.setSource(addressPostGisSource);
    addressPostGisLayer=new PostGisMapLayerControl();
    addressPostGisLayer.setMap(addressPostGisMap);

    String buildingPostGisSource="host=localhost,port=5432,database=tinel-gis,"
      +"schema=public,user=gis,password=gis,table=building,cache=false";
    DefaultMap buildingPostGisMap=new DefaultMap();
    buildingPostGisMap.setName("PostGIS Buildings");
    buildingPostGisMap.setSource(buildingPostGisSource);
    buildingPostGisLayer=new PostGisMapLayerControl();
    buildingPostGisLayer.setMap(buildingPostGisMap);

    String pointMemoryName="pointMemory";
    DefaultMap pointMemoryMap=new DefaultMap();
    pointMemoryMap.setName("In memory points");
    pointMemoryMap.setSource(pointMemoryName);
    pointMemoryLayer=new InMemoryMapLayerControl<com.vividsolutions.jts.geom.Point>();
    pointMemoryLayer.setMap(pointMemoryMap);
    pointMemoryLayer.setType(com.vividsolutions.jts.geom.Point.class);

    String lineMemoryName="lineMemory";
    DefaultMap lineMemoryMap=new DefaultMap();
    lineMemoryMap.setName("In memory lines");
    lineMemoryMap.setSource(lineMemoryName);
    lineMemoryLayer=new InMemoryMapLayerControl<LineString>();
    lineMemoryLayer.setMap(lineMemoryMap);
    lineMemoryLayer.setType(LineString.class);

    String polyMemoryName="polygonMemory";
    DefaultMap polyMemoryMap=new DefaultMap();
    polyMemoryMap.setName("In memory polygons");
    polyMemoryMap.setSource(polyMemoryName);
    polygonMemoryLayer=new InMemoryMapLayerControl<Polygon>();
    polygonMemoryLayer.setMap(polyMemoryMap);
    polygonMemoryLayer.setType(Polygon.class);

    String map1="Turisticna_avtokarta_Slovenije.png";
    DefaultMap turiMap=new DefaultMap();
    turiMap.setName(map1);
    turiMap.setSource(map1);
    WorldImageMapLayerControl turiRasterLayer=new WorldImageMapLayerControl();
    turiRasterLayer.setMap(turiMap);

    String map2="Adriatisches_Meer.png";
    DefaultMap adriaMap=new DefaultMap();
    adriaMap.setName(map2);
    adriaMap.setSource(map2);
    WorldImageMapLayerControl adriaRasterLayer=new WorldImageMapLayerControl();
    adriaRasterLayer.setMap(adriaMap);

    String map3="Celje.png";
    DefaultMap celjeMap=new DefaultMap();
    celjeMap.setName(map3);
    celjeMap.setSource(map3);
    WorldImageMapLayerControl celjeRasterLayer=new WorldImageMapLayerControl();
    celjeRasterLayer.setMap(celjeMap);

    /*
     * Init map layers
     */
    try {
      mapControl.afterPropertiesSet();

      // geoServerLayer.afterPropertiesSet();
      // adriaRasterLayer.afterPropertiesSet();
      // turiRasterLayer.afterPropertiesSet();
      // celjeRasterLayer.afterPropertiesSet();
      // worldShapeLayer.afterPropertiesSet();
      // addressPostGisLayer.afterPropertiesSet();
      streetPostGisLayer.afterPropertiesSet();
      // buildingPostGisLayer.afterPropertiesSet();
      pointMemoryLayer.afterPropertiesSet();
      lineMemoryLayer.afterPropertiesSet();
      polygonMemoryLayer.afterPropertiesSet();

    } catch (Exception e1) {
      e1.printStackTrace();
      return;
    }

    /*
     * Add map layers (order is important - last is on top!)
     */
    // mapControl.addLayer(geoServerLayer);
    // mapControl.addLayer(adriaRasterLayer);
    // mapControl.addLayer(turiRasterLayer);
    // mapControl.addLayer(celjeRasterLayer);
    // mapControl.addLayer(worldShapeLayer);
    mapControl.addLayer(streetPostGisLayer);
    // mapControl.addLayer(buildingPostGisLayer);
    // mapControl.addLayer(addressPostGisLayer);
    mapControl.addLayer(lineMemoryLayer);
    mapControl.addLayer(polygonMemoryLayer);
    mapControl.addLayer(pointMemoryLayer);

    renderer=new StreamingRenderer();
    renderer.setContext(mapControl.getMapContext());
  }

  protected void set() {
    /*
     * Set styles
     */
    StyleBuilder sb=new StyleBuilder();

    Stroke streetStroke=sb.createStroke(new Color(234, 224, 84), 5,
      StyleBuilder.LINE_JOIN_ROUND, StyleBuilder.LINE_CAP_ROUND);
    LineSymbolizer streetSymb=sb.createLineSymbolizer(streetStroke);
    streetPostGisLayer.getLayer().setStyle(sb.createStyle(streetSymb));

    // Mark mark=sb.createMark(StyleBuilder.MARK_SQUARE, Color
    // .decode("#808080"), Color.BLACK, 1);
    // Graphic gr=sb.createGraphic(null, mark, null, 0, 2, 0);
    // PointSymbolizer addSymb=sb.createPointSymbolizer(gr);
    // addressPostGisLayer.getLayer().setStyle(sb.createStyle(addSymb));

    Mark mark1=sb.createMark(StyleBuilder.MARK_SQUARE, Color.RED, new Color(
      178, 0, 0), 1);
    Graphic gr1=sb.createGraphic(null, mark1, null, 0, 5, 0);
    PointSymbolizer customSymb=sb.createPointSymbolizer(gr1);
    pointMemoryLayer.getLayer().setStyle(sb.createStyle(customSymb));

    Stroke lineStroke=sb.createStroke(Color.BLUE, 5,
      StyleBuilder.LINE_JOIN_ROUND, StyleBuilder.LINE_CAP_ROUND);
    LineSymbolizer lineSymb=sb.createLineSymbolizer(lineStroke);
    lineMemoryLayer.getLayer().setStyle(sb.createStyle(lineSymb));

    Symbolizer polySymb=sb.createPolygonSymbolizer(new Color(204, 204, 204),
      new Color(142, 142, 142), 2);
    polygonMemoryLayer.getLayer().setStyle(sb.createStyle(polySymb));

    /*
     * Set map characteristics
     */
    mapControl.setSize(mapPanel.getSize());

    mapControl.setScale(10000);

    // mapControl.setCenter(14.508301, 46.048793); // Ljubljana castle
    // mapControl.setCenter(-0.128197, 51.508274); // Trafalgar square, London
    // mapControl.setCenter(37.619845, 55.75367); // Lenin's Mausoleum, Moscow
    // mapControl.setCenter(15, 46);
    // mapControl.setCenter(-15, 30); // Canaria
    mapControl.setCenter(14.503, 46.05); // ~ Ljubljana Center
    // mapControl.setCenter(15.267, 46.233); // Celje
    // mapControl.setCenter(14.815417, 46.119944); // GEOSS
    // mapControl.setCenter(13.7309, 45.54); // Koper
    // mapControl.setCenter(13.8948, 44.7224); // Porer, Pula, Croatia
    // mapControl.setCenter(25.78, 71.17); // Northkap
    // mapControl.setCenter(-179, 45); // Date line
  }

  protected void getMap() {
    /*
     * Special adaption for projections
     */
    try {
      String name=(String)projectionCB.getSelectedItem();
      if (WmsEquidistantCylindricalProjectedCrs.class.getSimpleName().equals(
        name)) {
        // Center equidistant cylindrical projection
        Coordinate center=mapControl.getCenter();
        CoordinateReferenceSystem crs=new WmsEquidistantCylindricalProjectedCrs(
          center.y).getCrs();
        mapControl.setTargetCrs(crs);

      } else if (WmsAutoOrthographicProjectedCrs.class.getSimpleName().equals(
        name)) {
        // Center orthographic projection
        Coordinate center=mapControl.getCenter();
        CoordinateReferenceSystem crs=new WmsAutoOrthographicProjectedCrs(
          center.y, center.x).getCrs();
        mapControl.setTargetCrs(crs);
      }

    } catch (Exception e) {
      logger.error("", e);
    }

    /*
     * Prepare map control
     */
    Dimension mapSize=mapPanel.getSize();
    mapControl.setSize(mapSize);

    mapControl.prepare();

    /*
     * Render on buffer
     */
    buffer=new BufferedImage(mapSize.width, mapSize.height,
      BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d=buffer.createGraphics();

    // Java2D hints
    RenderingHints hints=new RenderingHints(Hints.KEY_ANTIALIASING,
      Hints.VALUE_ANTIALIAS_ON);
    // hints.put(Hints.KEY_DITHERING, Hints.VALUE_DITHER_ENABLE);
    hints.put(Hints.KEY_INTERPOLATION, Hints.VALUE_INTERPOLATION_BILINEAR);
    hints.put(Hints.KEY_RENDERING, Hints.VALUE_RENDER_QUALITY);
    // hints.put(Hints.IGNORE_COVERAGE_OVERVIEW, true);
    // hints.put(Hints.USE_JAI_IMAGEREAD, true);
    // hints.put(JAI.KEY_INTERPOLATION,
    // Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
    // hints.put(JAI.KEY_INTERPOLATION, new InterpolationBicubic2(32));
    // hints.put(JAI.KEY_INTERPOLATION, new InterpolationBicubic2(32));
    renderer.setJava2DHints(hints);

    // StreamingRenderer hints
    Map<Object, Object> rh=new HashMap<Object, Object>();
    // See StreamingRenderer.isOptimizedDataLoadingEnabled()
    rh.put(StreamingRenderer.OPTIMIZED_DATA_LOADING_KEY, true);
    renderer.setRendererHints(rh);

    renderer.paint(g2d, new Rectangle(mapSize), new ReferencedEnvelope(
      mapControl.getBounds(), mapControl.getTargetCrs()));

    /*
     * Paint panel
     */
    repaint();
  }

  protected void clear() {
    buffer=null;
    repaint();
  }

  protected void up() {
    mapControl.setSize(mapPanel.getSize());
    Coordinate c=mapControl.getCenter();
    int s=mapControl.getScale();
    mapControl.setCenter(c.x, c.y+s/2000000d);

    getMap();
  }

  protected void down() {
    mapControl.setSize(mapPanel.getSize());
    Coordinate c=mapControl.getCenter();
    int s=mapControl.getScale();
    mapControl.setCenter(c.x, c.y-s/2000000d);

    getMap();
  }

  protected void left() {
    mapControl.setSize(mapPanel.getSize());
    Coordinate c=mapControl.getCenter();
    int s=mapControl.getScale();
    mapControl.setCenter(c.x-s/2000000d, c.y);

    getMap();
  }

  protected void right() {
    mapControl.setSize(mapPanel.getSize());
    Coordinate c=mapControl.getCenter();
    int s=mapControl.getScale();
    mapControl.setCenter(c.x+s/2000000d, c.y);

    getMap();
  }

  protected void zoomIn() {
    mapControl.setSize(mapPanel.getSize());
    int s=mapControl.getScale();
    mapControl.setScale(s/3);

    getMap();
  }

  protected void zoomOut() {
    mapControl.setSize(mapPanel.getSize());
    int s=mapControl.getScale();
    mapControl.setScale(s*3);

    getMap();
  }

  protected void revGeoc() {
    Coordinate c=mapControl.screenToWorld(releasedX, releasedY);

    ReverseGeocodingDialog dialog=getReverseGeocodingDialog();
    dialog.showReverseGeocodingDialog(this, c);

    if (dialog.isCanceled()) return;

    net.tinelstudio.gis.reversegeocoding.locator.Locator locator=dialog
      .getLocator();
    c=locator.getSearchLocation();
    int radius=locator.getMaxDistanceMeters();

    setDrawnCircle(c.x, c.y, radius);

    try {
      List<? extends Place> places=getReverseGeocodingService().findNearest(
        locator);
      logger.info("Found geoplaces ["+places.size()+"]:");

      if (dialog.isClearAddresses()) {
        pointMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearStreets()) {
        lineMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearBuildings()) {
        polygonMemoryLayer.removeAllFeatures();
      }

      for (Place place : places) {
        // logger.info(place);
        if (place instanceof AddressDto) {
          AddressDto a=(AddressDto)place;
          pointMemoryLayer.addFeature(a.getPoint());

        } else if (place instanceof StreetDto) {
          StreetDto s=(StreetDto)place;
          lineMemoryLayer.addFeature(s.getLineString());

        } else if (place instanceof BuildingDto) {
          BuildingDto b=(BuildingDto)place;
          polygonMemoryLayer.addFeature(b.getPolygon());

        }
      }

    } catch (Exception e) {
      logger.error("Reverse Geocoding failed", e);
    }

    getMap();
  }

  protected void geoc() {
    GeocodingDialog dialog=getGeocodingDialog();
    dialog.showGeocodingDialog(this);

    if (dialog.isCanceled()) return;

    Locator locator=dialog.getLocator();

    try {
      List<? extends Place> places=getGeocodingService().find(locator);
      logger.info("Found geoplaces ["+places.size()+"]:");

      if (dialog.isClearAddresses()) {
        pointMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearStreets()) {
        lineMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearBuildings()) {
        polygonMemoryLayer.removeAllFeatures();
      }

      for (Place place : places) {
        logger.info(place);
        if (place instanceof AddressDto) {
          AddressDto a=(AddressDto)place;
          pointMemoryLayer.addFeature(a.getPoint());

        } else if (place instanceof StreetDto) {
          StreetDto s=(StreetDto)place;
          lineMemoryLayer.addFeature(s.getLineString());

        } else if (place instanceof BuildingDto) {
          BuildingDto b=(BuildingDto)place;
          polygonMemoryLayer.addFeature(b.getPolygon());

        }
      }

    } catch (Exception e) {
      logger.error("Geocoding failed", e);
    }

    getMap();
  }

  protected void route() {
    Coordinate a=mapControl.screenToWorld(pressedX, pressedY);
    Coordinate b=mapControl.screenToWorld(releasedX, releasedY);

    RoutingDialog dialog=getRoutingDialog();
    dialog.showRoutingDialog(this, a, b);

    if (dialog.isCanceled()) return;

    CustomRoutingParameters routingParameters=new CustomRoutingParameters();
    routingParameters.setStartLocation(dialog.getStartLocation());
    routingParameters.setGoalLocation(dialog.getGoalLocation());
    routingParameters.setCost(dialog.getCost());
    routingParameters.setHeuristic(dialog.getHeuristic());

    try {
      Route route=getRoutingService().findRoute(routingParameters);
      if (route==null) {
        logger.info("Route not found");
        return;
      }
      logger.info("Found route: "+route);

      if (dialog.isClearAddresses()) {
        pointMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearStreets()) {
        lineMemoryLayer.removeAllFeatures();
      }
      if (dialog.isClearBuildings()) {
        polygonMemoryLayer.removeAllFeatures();
      }

      LineString line=route.mergeStreets();
      lineMemoryLayer.addFeature(line);

    } catch (Exception e) {
      logger.error("Routing failed", e);
    }

    getMap();
  }

  protected void projection() {
    CrsFactory crsFactory;
    String name=(String)projectionCB.getSelectedItem();
    if (GoogleMapsProjectedCrs.class.getSimpleName().equals(name)) {
      crsFactory=new GoogleMapsProjectedCrs();

    } else if (SimpleMercatorProjectedCrs.class.getSimpleName().equals(name)) {
      crsFactory=new SimpleMercatorProjectedCrs();

    } else if (WmsEquirectangularProjectedCrs.class.getSimpleName()
      .equals(name)) {
      crsFactory=new WmsEquirectangularProjectedCrs();

    } else if (WmsEquidistantCylindricalProjectedCrs.class.getSimpleName()
      .equals(name)) {
      Coordinate center=mapControl.getCenter();
      crsFactory=new WmsEquidistantCylindricalProjectedCrs(center.y);

    } else if (WmsAutoOrthographicProjectedCrs.class.getSimpleName().equals(
      name)) {
      Coordinate center=mapControl.getCenter();
      crsFactory=new WmsAutoOrthographicProjectedCrs(center.y, center.x);

    } else {
      throw new IllegalStateException("Unkown CRS "+name);
    }

    try {
      mapControl.setTargetCrs(crsFactory.getCrs());

    } catch (Exception e) {
      logger.error("Projection set failed", e);
    }
  }

  private JComponent buildComponents() {
    /*
     * Buttons
     */
    this.b1=new JButton("Init");
    b1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        init();
      }
    });
    this.b4=new JButton("Set");
    b4.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        set();
      }
    });
    this.b2=new JButton("Get Map");
    b2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        getMap();
      }
    });
    this.b3=new JButton("Clear");
    b3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        clear();
      }
    });
    this.up=new JButton("U");
    up.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        up();
      }
    });
    this.down=new JButton("D");
    down.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        down();
      }
    });
    this.left=new JButton("L");
    left.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        left();
      }
    });
    this.right=new JButton("R");
    right.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        right();
      }
    });
    this.in=new JButton("+");
    in.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        zoomIn();
      }
    });
    this.out=new JButton("-");
    out.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        zoomOut();
      }
    });
    this.t1=new JButton("RevGeoc");
    t1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        revGeoc();
      }
    });
    this.t2=new JButton("Geoc");
    t2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        geoc();
      }
    });
    this.t3=new JButton("Route");
    t3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        route();
      }
    });

    /*
     * Combo box
     */
    this.projectionCB=new JComboBox();
    projectionCB.addItem(GoogleMapsProjectedCrs.class.getSimpleName());
    projectionCB.addItem(SimpleMercatorProjectedCrs.class.getSimpleName());
    projectionCB.addItem(WmsEquirectangularProjectedCrs.class.getSimpleName());
    projectionCB.addItem(WmsEquidistantCylindricalProjectedCrs.class
      .getSimpleName());
    projectionCB.addItem(WmsAutoOrthographicProjectedCrs.class.getSimpleName());
    projectionCB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        projection();
      }
    });

    /*
     * Context menu items
     */
    this.cm1=new JMenuItem("Find Nearest Geoplaces (Reverse Geocoding)");
    cm1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        revGeoc();
      }
    });
    this.cm2=new JMenuItem("Locate Geoplaces (Geocoding)");
    cm2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        geoc();
      }
    });
    this.cm3=new JMenuItem("Find Route (Routing)");
    cm3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        route();
      }
    });

    /*
     * Map panel
     */
    this.mapPanel=new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        /*
         * Draw map
         */
        if (buffer!=null) {
          g.drawImage(buffer, 0, 0, null);
        }

        /*
         * Print some stuff
         */
        // logger.debug("Size="+mapPanel.getSize());
        //
        // float lineKm=0.01f;
        // float lineP=lineKm/0.0254f
        // *Toolkit.getDefaultToolkit().getScreenResolution();
        // float w2=mapPanel.getWidth()/2;
        // int h2=mapPanel.getHeight()/2;
        // int lw2=Math.round(w2-lineP/2);
        // int rw2=Math.round(w2+lineP/2);
        //
        // // g.setXORMode(Color.BLACK);
        // // g.drawLine(lw2, h2, rw2, h2);
        //
        // // g.fillRect(lw2, h2-2, Math.round(lineP), 4);
        //
        // Coordinate lc=mapControl.screenToWorld(lw2, h2);
        // Coordinate rc=mapControl.screenToWorld(rw2, h2);
        //
        // int dist=-1;
        // if (lc!=null&&rc!=null) {
        // logger.debug(lw2+", "+h2+" = "+lc);
        // logger.debug(rw2+", "+h2+" = "+rc);
        //
        // dist=DistanceOnEarth.distance(lc.y, lc.x, rc.y, rc.x);
        // }

        long s=mapControl.getScale();

        // logger.debug("Wanted dist="+lineKm*s+" m; Real dist="+dist+" m");

        /*
         * Draw circle
         */
        if (drawnCircleRadius>0&&s>0) {
          g.setColor(Color.BLACK);
          double lat=drawnCircleLat;
          double tlx=DistanceOnEarth.longitudeAtDistance(drawnCircleLng,
            -drawnCircleRadius, lat);
          double tly=DistanceOnEarth.latitudeAtDistance(drawnCircleLat,
            -drawnCircleRadius);
          double rbx=DistanceOnEarth.longitudeAtDistance(drawnCircleLng,
            drawnCircleRadius, lat);
          double rby=DistanceOnEarth.latitudeAtDistance(drawnCircleLat,
            drawnCircleRadius);

          Point tl=mapControl.worldToScreen(tlx, tly);
          Point br=mapControl.worldToScreen(rbx, rby);

          if (tl!=null&&br!=null) {
            g.drawOval(tl.x, tl.y, br.x-tl.x, br.y-tl.y);
          }
        }

        /*
         * Draw some locations
         */
        // g.setColor(Color.CYAN);
        //
        // // Ljubljana castle
        // Point p=mapControl.worldToScreen(14.508301, 46.048793);
        // if (p!=null) {
        // g.fillRect(p.x-3, p.y-3, 6, 6);
        // }
        //
        // // Trafalgar square, London
        // p=mapControl.worldToScreen(-0.128197, 51.508274);
        // if (p!=null) {
        // g.fillRect((int)p.getX()-3, (int)p.getY()-3, 6, 6);
        // }
        //
        // // Lenin's Mausoleum, Moscow
        // p=mapControl.worldToScreen(37.619845, 55.75367);
        // if (p!=null) {
        // g.fillRect((int)p.getX()-3, (int)p.getY()-3, 6, 6);
        // }
        //
        // // North Cape, Norway
        // p=mapControl.worldToScreen(25.78, 71.17);
        // if (p!=null) {
        // g.fillRect((int)p.getX()-3, (int)p.getY()-3, 6, 6);
        // }
      }
    };
    mapPanel.addMouseMotionListener(new MouseAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        Coordinate c=mapControl.screenToWorld(e.getX(), e.getY());
        if (c==null) return;
        coorsL.setText("mouse lng="+c.x+", lat="+c.y);
      }
    });
    mapPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        pressedX=e.getX();
        pressedY=e.getY();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          releasedX=e.getX();
          releasedY=e.getY();
          contextMenu.show(mapPanel, e.getX(), e.getY());
        }
      }
    });
    mapPanel.setBackground(Color.WHITE);

    /*
     * Status bar
     */
    this.coorsL=new JLabel("mouse lnglat 0123456789");
    JToolBar statusBar=new JToolBar();
    statusBar.add(coorsL);

    /*
     * Context menu
     */
    this.contextMenu=new JPopupMenu();
    contextMenu.add(cm2);
    contextMenu.add(cm1);
    contextMenu.add(cm3);

    /*
     * Button bar
     */
    JPanel buttonPanel=new JPanel(new FlowLayout());
    buttonPanel.add(projectionCB);
    // buttonPanel.add(b1);
    buttonPanel.add(b2);
    buttonPanel.add(b4);
    // buttonPanel.add(b3);
    buttonPanel.add(up);
    buttonPanel.add(down);
    buttonPanel.add(left);
    buttonPanel.add(right);
    buttonPanel.add(in);
    buttonPanel.add(out);
    buttonPanel.add(t2);
    buttonPanel.add(t1);
    buttonPanel.add(t3);

    JPanel mainPanel=new JPanel(new BorderLayout());
    mainPanel.add(buttonPanel, BorderLayout.NORTH);
    mainPanel.add(mapPanel, BorderLayout.CENTER);
    mainPanel.add(statusBar, BorderLayout.SOUTH);
    return mainPanel;
  }

  private void setDrawnCircle(double lng, double lat, int radiusM) {
    this.drawnCircleLng=lng;
    this.drawnCircleLat=lat;
    this.drawnCircleRadius=radiusM;
  }

  // #################################################

  private ReverseGeocodingService getReverseGeocodingService() {
    if (this.reverseGeocodingService==null) {
      HttpInvokerProxyFactoryBean httpInvoker=new HttpInvokerProxyFactoryBean();
      httpInvoker.setServiceInterface(ReverseGeocodingService.class);
      httpInvoker
      // .setServiceUrl("http://localhost:9081/tinel-gis-reversegeocoding/remoting/ReverseGeocodingService");
        .setServiceUrl("http://localhost:8080/tinel-gis-reversegeocoding/remoting/ReverseGeocodingService");
      // .setServiceUrl("http://win7:8080/tinel-gis-reversegeocoding/remoting/ReverseGeocodingService");
      httpInvoker.afterPropertiesSet();
      this.reverseGeocodingService=(ReverseGeocodingService)httpInvoker
        .getObject();
    }
    return this.reverseGeocodingService;
  }

  private GeocodingService getGeocodingService() {
    if (this.geocodingService==null) {
      HttpInvokerProxyFactoryBean httpInvoker=new HttpInvokerProxyFactoryBean();
      httpInvoker.setServiceInterface(GeocodingService.class);
      httpInvoker
      // .setServiceUrl("http://localhost:9082/tinel-gis-geocoding/remoting/GeocodingService");
        .setServiceUrl("http://localhost:8080/tinel-gis-geocoding/remoting/GeocodingService");
      // .setServiceUrl("http://win7:8080/tinel-gis-geocoding/remoting/GeocodingService");
      httpInvoker.afterPropertiesSet();
      this.geocodingService=(GeocodingService)httpInvoker.getObject();
    }
    return this.geocodingService;
  }

  private RoutingService getRoutingService() {
    if (this.routingService==null) {
      HttpInvokerProxyFactoryBean httpInvoker=new HttpInvokerProxyFactoryBean();
      httpInvoker.setServiceInterface(RoutingService.class);
      httpInvoker
      // .setServiceUrl("http://localhost:9083/tinel-gis-routing/remoting/RoutingService");
        .setServiceUrl("http://localhost:8080/tinel-gis-routing/remoting/RoutingService");
      // .setServiceUrl("http://win7:8080/tinel-gis-routing/remoting/RoutingService");
      httpInvoker.afterPropertiesSet();
      this.routingService=(RoutingService)httpInvoker.getObject();
    }
    return this.routingService;
  }

  public GeocodingDialog getGeocodingDialog() {
    if (this.geocodingDialog==null) {
      this.geocodingDialog=new GeocodingDialog();
    }
    return this.geocodingDialog;
  }

  public void setGeocodingDialog(GeocodingDialog geocodingDialog) {
    this.geocodingDialog=geocodingDialog;
  }

  public ReverseGeocodingDialog getReverseGeocodingDialog() {
    if (this.reverseGeocodingDialog==null) {
      this.reverseGeocodingDialog=new ReverseGeocodingDialog();
    }
    return this.reverseGeocodingDialog;
  }

  public void setReverseGeocodingDialog(
    ReverseGeocodingDialog reverseGeocodingDialog) {
    this.reverseGeocodingDialog=reverseGeocodingDialog;
  }

  public RoutingDialog getRoutingDialog() {
    if (this.routingDialog==null) {
      this.routingDialog=new RoutingDialog();
    }
    return this.routingDialog;
  }

  public void setRoutingDialog(RoutingDialog routingDialog) {
    this.routingDialog=routingDialog;
  }
}
