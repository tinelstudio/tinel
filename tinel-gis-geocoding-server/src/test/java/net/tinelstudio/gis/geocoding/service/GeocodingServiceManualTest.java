/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.service;

import java.util.List;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.geocoding.SpringContexts;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.StreetLocator;

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
  SpringContexts.GEOCODING_SERVICE_CONTEXT})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
public class GeocodingServiceManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private GeocodingService geocodingService;

  // @SuppressWarnings("unused")
  // @Test
  // public void testGeocodingService() throws Exception {
  // Locator locator=null;
  // List<?> results=this.geocodingService.find(locator);
  // }
  //
  // @SuppressWarnings("unused")
  // public void example1() {
  // Locator locator=new FallbackLocator(new CompositeLocator(
  // new StreetLocator(), new BuildingLocator()),
  // new FallbackLocator(new DistinctAddressByTownLocator(),
  // new DistinctAddressByCountryLocator()));
  // }
  //
  // @SuppressWarnings("unused")
  // public void example2() throws Exception {
  // Locator locator=new AddressLocator();
  // locator.setSearchString("cank ul. 3");
  // locator.setMaxResults(10);
  // List<? extends Place> places=this.geocodingService.find(locator);
  // }

  @Test
  // @Repeat(100)
  public void testFind() throws Exception {
    // String searchString="Cank 1";
    // String searchString="Cank";
    String searchString="slov";
    int maxResults=10000;

    // CompositeLocator l= new CompositeLocator();
    // l.setLocators(new StreetLocator(), new AddressLocator());
    // Locator l=new AddressLocator();
    // Locator l= new DistinctAddressByTownLocator();
    // Locator l= new DistinctAddressByCountryLocator();
    // DefaultFallbackLocator l= new DefaultFallbackLocator();
    // Locator l= new FullCompositeLocator();
    // Locator l= new BuildingLocator();
    // Locator l= new DistinctBuildingByCountryLocator();
    Locator l=new StreetLocator();
    // Locator l= new DistinctStreetByCountryLocator();

    l.setMaxResults(maxResults);
    l.setSearchString(searchString);
    List<? extends Place> places=this.geocodingService.find(l);
    logger.info("Found geoplaces ["+places.size()+"]:");
    for (Place place : places) {
      logger.info(place);
    }
  }
}
