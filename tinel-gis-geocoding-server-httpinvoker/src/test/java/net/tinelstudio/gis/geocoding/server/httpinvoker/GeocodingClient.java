/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.server.httpinvoker;

import java.util.List;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.geocoding.locator.AddressLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.service.GeocodingService;

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
public class GeocodingClient {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private GeocodingService geocodingService;

  @Test
  // @Repeat(100)
  public void testFind() throws Exception {
    String searchString="Cank 1";
    // String searchString="Cank";
    // String searchString="slov";
    int maxResults=10000;

    // CompositeLocator l= new CompositeLocator();
    // l.setLocators(new StreetLocator(), new AddressLocator());
    Locator l=new AddressLocator();
    // Locator l= new DistinctAddressByTownLocator();
    // Locator l= new DistinctAddressByCountryLocator();
    // DefaultFallbackLocator l= new DefaultFallbackLocator();
    // Locator l= new FullCompositeLocator();
    // Locator l= new BuildingLocator();
    // Locator l= new DistinctBuildingByCountryLocator();
    // Locator l= new StreetLocator();
    // Locator l= new DistinctStreetByCountryLocator();

    l.setMaxResults(maxResults);
    l.setSearchString(searchString);
    List<? extends Place> res=this.geocodingService.find(l);
    logger.info("Found geoplaces ["+res.size()+"]:");
    for (Place place : res) {
      logger.info(place);
    }
  }
}
