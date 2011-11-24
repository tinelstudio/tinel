/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image;

import net.tinelstudio.gis.client.control.MapControl;
import net.tinelstudio.gis.client.control.image.layer.ImageMapLayerControl;

import org.geotools.map.MapContext;

/**
 * @author TineL
 */
public interface ImageMapControl extends MapControl {

  MapContext getMapContext();

  void addLayer(ImageMapLayerControl layer);

  void insertLayer(ImageMapLayerControl layer, int index);

  void removeLayer(ImageMapLayerControl layer);
}
