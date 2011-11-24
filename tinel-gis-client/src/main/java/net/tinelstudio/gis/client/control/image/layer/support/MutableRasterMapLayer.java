/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer.support;

import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;

/**
 * @author TineL
 */
public class MutableRasterMapLayer extends MutableMapLayer {

  public MutableRasterMapLayer(String title) {
    super(createRasterStyle(), title);
  }

  public static Style createRasterStyle() {
    StyleBuilder sb=new StyleBuilder();
    RasterSymbolizer rs=sb.createRasterSymbolizer();
    Style style=sb.createStyle(rs);
    return style;
  }
}
