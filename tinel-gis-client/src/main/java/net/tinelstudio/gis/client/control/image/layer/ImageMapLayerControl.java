/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import net.tinelstudio.gis.client.control.image.ImageMapControl;
import net.tinelstudio.gis.client.domain.Map;

import org.geotools.map.MapLayer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author TineL
 */
public interface ImageMapLayerControl extends InitializingBean, DisposableBean {

  Map getMap();

  void setMap(Map map);

  MapLayer getLayer();

  void prepare(ImageMapControl mapControl);
}
