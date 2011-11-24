/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.server.httpinvoker;

import java.util.Date;

import net.tinelstudio.gis.geocoding.service.GeocodingService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The actual server that runs the service.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringTestContexts.JETTY_CONTEXT,
  SpringTestContexts.CLIENT_CONTEXT})
public class GeocodingServer {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private GeocodingService geocodingService;

  @Test
  public void startJetty() throws Exception {
    logger
      .info("Server time: "+new Date(this.geocodingService.getServerTime()));

    // Wait until Jetty stops
    Thread.currentThread().join();
  }
}
