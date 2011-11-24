/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import java.awt.Color;
import java.io.File;

import net.tinelstudio.gis.client.control.image.ImageMapControl;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author TineL
 */
public class ShapeMapLayerControl extends AbstractImageMapLayerControl {

  private DataStore dataStore;

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    String source=getMap().getSource();

    File file=new File(source);

    // Connection parameters
    // Map<String, Serializable> connectParameters=new HashMap<String,
    // Serializable>();
    //
    // connectParameters.put("url", file.toURI().toURL());
    // connectParameters.put("create spatial index", true);
    // dataStore=DataStoreFinder.getDataStore(connectParameters);

    this.dataStore=new ShapefileDataStore(file.toURI().toURL());
    // ((ShapefileDataStore)dataStore).forceSchemaCRS(DefaultGeographicCRS.WGS84);

    String[] typeNames=getDataStore().getTypeNames();
    String typeName=typeNames[0];

    logger.info("Reading content '"+typeName+"'");

    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource=getDataStore()
      .getFeatureSource(typeName);

    logger.info("Loaded content '"+typeName+"'");

    // Some style
    StyleBuilder sb=new StyleBuilder();
    PolygonSymbolizer rs=sb.createPolygonSymbolizer(Color.RED, 2);
    Style style=sb.createStyle(rs);

    MapLayer layer=new DefaultMapLayer(featureSource, style);
    setLayer(layer);
  }

  @Override
  public void destroy() throws Exception {
    getDataStore().dispose();
  }

  @Override
  public void prepare(ImageMapControl mapControl) {}

  private DataStore getDataStore() {
    return this.dataStore;
  }
}
