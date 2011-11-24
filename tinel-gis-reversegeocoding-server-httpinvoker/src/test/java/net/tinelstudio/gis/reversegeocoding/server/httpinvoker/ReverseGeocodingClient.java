/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.server.httpinvoker;

import java.util.List;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.reversegeocoding.locator.AddressLocator;
import net.tinelstudio.gis.reversegeocoding.locator.Locator;
import net.tinelstudio.gis.reversegeocoding.service.ReverseGeocodingService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * The client to test the connection.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringTestContexts.CLIENT_CONTEXT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
public class ReverseGeocodingClient {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private ReverseGeocodingService reverseGeocodingService;

  @Test
  // @Repeat(100)
  public void testFindNearest() throws Exception {
    Coordinate c=new Coordinate(15, 46);
    int maxDistanceMeters=500;
    int maxResults=1000;

    Locator locator=new AddressLocator();
    // Locator locator=new StreetLocator();
    // Locator locator=new BuildingLocator();

    locator.setSearchLocation(c);
    locator.setMaxDistanceMeters(maxDistanceMeters);
    locator.setMaxResults(maxResults);
    List<? extends Place> places=this.reverseGeocodingService
      .findNearest(locator);
    logger.info("Found geoplaces ["+places.size()+"]:");
    // for (Place place : places) {
    // logger.info(place);
    // }
  }
}
