/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import java.awt.Color;

import net.tinelstudio.gis.client.control.image.ImageMapControl;
import net.tinelstudio.gis.client.control.image.layer.support.ClearableMemoryDataStore;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.Symbolizer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author TineL
 * @param <T> the geometry type, see {@link #setType(Class)}
 */
public class InMemoryMapLayerControl<T extends Geometry> extends
  AbstractImageMapLayerControl {

  private ClearableMemoryDataStore dataStore;

  private SimpleFeatureType simpleFeatureType;

  private Class<T> type;

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();
    Assert.notNull(getType(), "Property 'type' not set");

    String schemaName=getType().getSimpleName();
    this.simpleFeatureType=DataUtilities.createType(schemaName, "shape:"
      +schemaName+":srid=4326"); // see createFeatureType();

    this.dataStore=new ClearableMemoryDataStore();

    this.dataStore.createSchema(simpleFeatureType);
    // ((ShapefileDataStore)dataStore).forceSchemaCRS(DefaultGeographicCRS.WGS84);

    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource=getDataStore()
      .getFeatureSource(schemaName);

    // Some style
    StyleBuilder sb=new StyleBuilder();
    Symbolizer rs=sb.createPolygonSymbolizer(Color.ORANGE, 1);
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

  private ClearableMemoryDataStore getDataStore() {
    return this.dataStore;
  }

  public void addFeature(T geometry) {
    SimpleFeature feature=SimpleFeatureBuilder.build(simpleFeatureType,
      new Object[]{geometry}, SimpleFeatureBuilder.createDefaultFeatureId());
    getDataStore().addFeature(feature);
  }

  public void removeAllFeatures() {
    getDataStore().removeAllFeatures(getType().getSimpleName());
  }

  public Class<T> getType() {
    return this.type;
  }

  public void setType(Class<T> type) {
    this.type=type;
  }
}