/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer;

import java.io.File;

import net.tinelstudio.gis.client.control.image.ImageMapControl;
import net.tinelstudio.gis.client.control.image.layer.support.MutableRasterMapLayer;

import org.geotools.gce.image.WorldImageReader;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;

/**
 * @author TineL
 */
public class WorldImageMapLayerControl extends AbstractImageMapLayerControl {

  private WorldImageReader reader;

  @Override
  public void afterPropertiesSet() throws Exception {
    super.afterPropertiesSet();

    String source=getMap().getSource();

    File imageFile=new File(source);
    this.reader=new WorldImageReader(imageFile);

    MapLayer layer=new DefaultMapLayer(getReader(), MutableRasterMapLayer
      .createRasterStyle());
    setLayer(layer);

    logger.debug("CRS = "+getReader().getCrs().toWKT());
    logger.debug("bounds = "+getReader().getOriginalEnvelope());
  }

  @Override
  public void destroy() throws Exception {
    getReader().dispose();
  }

  @Override
  public void prepare(ImageMapControl mapControl) {}

  private WorldImageReader getReader() {
    return this.reader;
  }
}
