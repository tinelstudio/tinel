/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.service;

import java.util.List;

import net.tinelstudio.gis.common.MaxResultsLimitExceededException;
import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.ServiceException;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.common.service.AbstractEnablableService;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.LocatorService;
import net.tinelstudio.gis.geocoding.locator.LocatorServiceFactory;
import net.tinelstudio.gis.model.dao.FindingDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

/**
 * The server side implementation of the Geocoding service.
 * 
 * @author TineL
 */
public class GeocodingServiceImpl extends AbstractEnablableService implements
  GeocodingService {

  private final Log logger=LogFactory.getLog(getClass());

  private int maxResultsLimit=10000;

  private FindingDao findingDao;

  private LocatorServiceFactory locatorServiceFactory;

  @Transactional(readOnly=true)
  @Override
  public List<? extends Place> find(Locator locator)
    throws NotEnabledException, MaxResultsLimitExceededException,
    ServiceException {
    if (!isEnabled()) throw new NotEnabledException();

    if (logger.isDebugEnabled()) {
      logger.debug("Calling Geocoding service with locator="+locator);
    }

    StopWatch sw=new StopWatch();
    sw.start();

    // Check maxResults limit
    if (this.maxResultsLimit>0) {
      // Limit is applied
      int maxResults=locator.getMaxResults();
      if (maxResults<=0) {
        throw new MaxResultsLimitExceededException(
          "maxResults=unlimited > maxResultsLimit="+this.maxResultsLimit);

      } else if (maxResults>this.maxResultsLimit) {
        throw new MaxResultsLimitExceededException("maxResults="+maxResults
          +" > maxResultsLimit="+this.maxResultsLimit);
      }
    }

    List<? extends Place> placesFound;
    try {
      // Get locator service
      LocatorService locatorService=getLocatorServiceFactory()
        .createLocatorService(locator, getFindingDao());

      // Find
      placesFound=locatorService.find();

    } catch (Throwable t) {
      logger.error("", t);
      throw new ServiceException(t);
    }

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found geoplaces ["+placesFound.size()+"] for locator="
        +locator+" in "+sw.getLastTaskTimeMillis()+" ms");
    }

    return placesFound;
  }

  @Override
  public int getMaxResultsLimit() throws NotEnabledException {
    if (!isEnabled()) throw new NotEnabledException();
    return this.maxResultsLimit;
  }

  /**
   * @param maxResultsLimit the service maximum results limit, or <= 0 with no
   *        limit
   * @see #getMaxResultsLimit()
   */
  public void setMaxResultsLimit(int maxResultsLimit) {
    this.maxResultsLimit=maxResultsLimit;
  }

  @Override
  public long getServerTime() {
    return System.currentTimeMillis();
  }

  // ########################################

  public LocatorServiceFactory getLocatorServiceFactory() {
    if (this.locatorServiceFactory==null) {
      this.locatorServiceFactory=new LocatorServiceFactory();
    }
    return this.locatorServiceFactory;
  }

  public void setLocatorServiceFactory(
    LocatorServiceFactory locatorServiceFactory) {
    this.locatorServiceFactory=locatorServiceFactory;
  }

  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }
}
