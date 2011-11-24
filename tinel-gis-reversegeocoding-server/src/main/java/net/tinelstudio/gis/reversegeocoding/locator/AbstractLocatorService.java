/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import net.tinelstudio.gis.model.dao.FindingDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * The abstract implementation of {@link LocatorService}.
 * 
 * @author TineL
 */
public abstract class AbstractLocatorService extends AbstractLocator implements
  LocatorService {

  protected final Log logger=LogFactory.getLog(getClass());

  private GeometryFactory geometryFactory;

  private FindingDao findingDao;

  // ##################################################################

  /**
   * Converts the given coordinate to a {@link Point} using
   * {@link #getGeometryFactory()}.
   * 
   * @param c the coordinate to convert (<code>null</code> permitted)
   * @return a point (<code>null</code> possible)
   */
  protected Point convertToPoint(Coordinate c) {
    if (c==null) return null;
    return getGeometryFactory().createPoint(c);
  }

  // ##################################################################

  public GeometryFactory getGeometryFactory() {
    if (this.geometryFactory==null) {
      this.geometryFactory=new GeometryFactory(new PrecisionModel(), 4326);
    }
    return this.geometryFactory;
  }

  public void setGeometryFactory(GeometryFactory geometryFactory) {
    this.geometryFactory=geometryFactory;
  }

  @Override
  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  @Override
  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }
}
