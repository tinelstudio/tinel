/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.compass;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.CompassHits;
import org.compass.core.CompassTemplate;
import org.compass.gps.CompassGps;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

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
  SpringContexts.COMPASS_CONTEXT})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
public class CompassManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private CompassTemplate compassTemplate;

  @Autowired
  private CompassGps hibernateGps;

  // @Test
  public void testIndex() throws Exception {
    logger.info("Start indexing...");
    hibernateGps.index();
    // hibernateGps.index(GeoName.class);
    logger.info("Indexing done");
  }

  // @SuppressWarnings("unused")
  // public void example1() {
  // String query="GeoName.name:ljub* AND GeoName.type=TOWN";
  // }
  //
  // @SuppressWarnings("unused")
  // public void example2() {
  // String query="GeoName.name:cank* AND GeoName.type:STREET";
  // CompassHits hits=this.compassTemplate.find(query);
  // }

  @Transactional(readOnly=true)
  @Test
  // @Repeat(10)
  public void testCompass() throws Exception {
    String name="slov";

    String query="GeoName.name:"+name+"*";
    // "GeoName.name:"+name+"* AND GeoName.type:"+Type.STREET;
    // "(GeoName.name:"+name+"* AND GeoName.type:ADDRESS)^4.0 OR " +
    // "(GeoName.name:"+name+"* AND GeoName.type:STREET)^3.5 OR "+
    // "(GeoName.name:"+name+"* AND GeoName.type:BUILDING)^3.0 OR "+
    // "(GeoName.name:"+name+"* AND GeoName.type:TOWN)^2.5 OR "+
    // "(GeoName.name:"+name+"* AND GeoName.type:REGION)^2.0 OR "+
    // "(GeoName.name:"+name+"* AND GeoName.type:COUNTRY)^1.5 OR "+

    CompassHits hits=this.compassTemplate.find(query);

    logger.info("Query: "+query);
    logger.info("Suggested query: "+hits.getQuery());
    logger.info("Compass hits: "+hits.getLength());
    if (hits.getLength()>0) {
      for (int i=0; i<hits.length()&&i<1000; i++) {
        logger.info("Hit #"+(i+1)+": "+hits.data(i));
      }
    }
  }
}
