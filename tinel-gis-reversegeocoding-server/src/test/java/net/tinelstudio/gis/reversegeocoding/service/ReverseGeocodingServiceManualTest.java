/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.service;

import java.util.List;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.reversegeocoding.SpringContexts;
import net.tinelstudio.gis.reversegeocoding.locator.AddressLocator;
import net.tinelstudio.gis.reversegeocoding.locator.Locator;

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
 * Manual tests.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
  net.tinelstudio.gis.model.SpringContexts.DB_CONTEXT,
  SpringContexts.REVERSE_GEOCODING_SERVICE_CONTEXT})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
public class ReverseGeocodingServiceManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private ReverseGeocodingService reverseGeocodingService;

  // @SuppressWarnings("unused")
  // public void example1() {
  // Locator locator=new FallbackLocator(new CompositeLocator(
  // new AddressLocator(), new BuildingLocator()), new StreetLocator());
  // }
  //
  // @SuppressWarnings("unused")
  // public void example2() throws Exception {
  // Locator locator=new AddressLocator();
  // locator.setSearchLocation(new Coordinate(15.654, 46.548));
  // locator.setMaxDistanceMeters(500);
  // locator.setMaxResults(10);
  // List<? extends Place> places=this.reverseGeocodingService
  // .findNearest(locator);
  // }

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
