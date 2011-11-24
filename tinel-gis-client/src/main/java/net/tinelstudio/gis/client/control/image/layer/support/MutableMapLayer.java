/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer.support;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.event.MapLayerEvent;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.operation.TransformException;

/**
 * @author TineL
 */
public class MutableMapLayer extends DefaultMapLayer {

  public MutableMapLayer(Style style, String title) {
    super((FeatureSource<SimpleFeatureType, SimpleFeature>)null, style, title);
  }

  public MutableMapLayer(
    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Style style,
    String title) {
    super(featureSource, style, title);
  }

  public void setFeatureSource(
    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource) {
    this.featureSource=featureSource;

    // Notify
    fireMapLayerListenerLayerChanged(new MapLayerEvent(this,
      MapLayerEvent.DATA_CHANGED));
  }

  public void setGridCoverage(GridCoverage2D coverage)
    throws TransformException, SchemaException, IllegalAttributeException {
    if (coverage!=null) {
      setFeatureSource(DataUtilities.source(FeatureUtilities
        .wrapGridCoverage(coverage)));

    } else {
      setFeatureSource(null);
    }
  }
}
