/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.server.httpinvoker;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import net.tinelstudio.gis.reversegeocoding.service.ReverseGeocodingService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Starts up Jetty server (defined in Jetty context) and client (defined in
 * client context). Jetty server starts web application from web.xml. Web
 * application starts the service to which the client connects.
 * 
 * @author TineL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringTestContexts.JETTY_CONTEXT,
  SpringTestContexts.CLIENT_CONTEXT})
public class ReverseGeocodingIntegrationTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private ReverseGeocodingService reverseGeocodingService;

  @Test
  public void testIntegrity() throws Exception {
    assertNotNull(this.reverseGeocodingService);

    logger.info("ReverseGeocodingService maxResultsLimit="
      +this.reverseGeocodingService.getMaxResultsLimit());
    logger.info("ReverseGeocodingService serverTime="
      +new Date(this.reverseGeocodingService.getServerTime()));
  }
}
