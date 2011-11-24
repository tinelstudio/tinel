/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.tinelstudio.gis.client.control.image.ImageMapControl;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author TineL
 */
public class PostGisMapLayerControl extends AbstractImageMapLayerControl {

  private DataStore dataStore;

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    String source=getMap().getSource();

    String[] c=source.split(",");
    Map<String, String> sourceMap=new HashMap<String, String>();
    for (String conf : c) {
      String[] confS=conf.split("=");
      sourceMap.put(confS[0], confS[1]);
    }

    Map<String, Object> config=new HashMap<String, Object>();
    config.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
    config.put(PostgisDataStoreFactory.HOST.key, sourceMap.get("host"));
    config.put(PostgisDataStoreFactory.PORT.key, Integer.parseInt(sourceMap
      .get("port")));
    config.put(PostgisDataStoreFactory.DATABASE.key, sourceMap.get("database"));
    config.put(PostgisDataStoreFactory.SCHEMA.key, sourceMap.get("schema"));
    config.put(PostgisDataStoreFactory.USER.key, sourceMap.get("user"));
    config.put(PostgisDataStoreFactory.PASSWD.key, sourceMap.get("password"));
    // config.put(PostgisDataStoreFactory.ESTIMATEDEXTENT.key, true);
    String tableName=sourceMap.get("table");
    boolean cache=Boolean.parseBoolean(sourceMap.get("cache"));

    // Map<String, Object> config=new HashMap<String, Object>();
    // config.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
    // config.put(PostgisDataStoreFactory.HOST.key, "localhost");
    // config.put(PostgisDataStoreFactory.PORT.key, 5432);
    // config.put(PostgisDataStoreFactory.DATABASE.key, "gis");
    // config.put(PostgisDataStoreFactory.SCHEMA.key, "public");
    // config.put(PostgisDataStoreFactory.USER.key, "gis");
    // config.put(PostgisDataStoreFactory.PASSWD.key, "gis");
    // String tableName="street";
    // boolean cache=true;

    // File file=new File(source);

    // Connection parameters
    // Map<String, Serializable> connectParameters=new HashMap<String,
    // Serializable>();
    //
    // connectParameters.put("url", file.toURI().toURL());
    // connectParameters.put("create spatial index", true);
    // dataStore=DataStoreFinder.getDataStore(connectParameters);

    this.dataStore=new PostgisDataStoreFactory().createDataStore(config);
    // ((ShapefileDataStore)dataStore).forceSchemaCRS(DefaultGeographicCRS.WGS84);

    // We are now connected

    // String[] typeNames=dataStore.getTypeNames();
    // String typeName=null;
    // for (String tn : typeNames) {
    // if (tn.equals(tableName)) {
    // typeName=tn;
    // break;
    // }
    // }
    String typeName=tableName;

    // Assert.notNull(typeName, "Table name '"+tableName+"' not found");

    logger.info("Reading content '"+typeName+"'");

    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource=getDataStore()
      .getFeatureSource(typeName);

    if (cache) {
      MemoryDataStore memoryDataStore=new MemoryDataStore(featureSource
        .getFeatures());
      featureSource=memoryDataStore.getFeatureSource(typeName);
    }

    logger.info("Loaded content '"+typeName+"'");

    // Some style
    StyleBuilder sb=new StyleBuilder();
    LineSymbolizer rs=sb.createLineSymbolizer(Color.BLUE, 1);
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
