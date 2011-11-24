/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.server.httpinvoker;

import java.util.Date;

import net.tinelstudio.gis.routing.service.RoutingService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringTestContexts.JETTY_CONTEXT,
  SpringTestContexts.CLIENT_CONTEXT})
public class RoutingServer {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private RoutingService routingService;

  @Test
  public void startJetty() throws Exception {
    logger.info("Server time: "+new Date(this.routingService.getServerTime()));

    // Wait until Jetty stops
    Thread.currentThread().join();
  }
}
