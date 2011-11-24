/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import net.tinelstudio.gis.client.domain.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.map.MapLayer;
import org.springframework.util.Assert;

/**
 * @author TineL
 */
public abstract class AbstractImageMapLayerControl implements
  ImageMapLayerControl {

  protected final Log logger=LogFactory.getLog(getClass());

  private Map map;

  private MapLayer mapLayer;

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(this.map, "Property 'map' not set");
  }

  @Override
  public Map getMap() {
    return this.map;
  }

  @Override
  public void setMap(Map map) {
    this.map=map;
  }

  public MapLayer getLayer() {
    Assert.notNull(this.mapLayer, "Map layer has not been initialized");
    return this.mapLayer;
  }

  protected void setLayer(MapLayer layer) {
    this.mapLayer=layer;
  }
}