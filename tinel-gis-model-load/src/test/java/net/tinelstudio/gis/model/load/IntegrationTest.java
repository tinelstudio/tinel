/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TineL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringContexts.LOAD_CONTEXT,
  TestSpringContexts.TEST_CONTEXT})
public class IntegrationTest {

  @Resource
  private AddressMaker addressMaker;
  @Autowired
  private BuildingMaker buildingMaker;
  @Resource
  private InMemoryGeoNameMaker inMemoryGeoNameMaker;
  @Resource
  private InMemoryStreetNodeMaker inMemoryStreetNodeMaker;
  @Autowired
  private StreetMaker streetMaker;

  @Test
  public void testIntegrity() {
    assertNotNull(addressMaker.getGeometryFactory());
    assertNotNull(addressMaker.getGeoNameDao());

    assertNotNull(buildingMaker.getGeoNameDao());

    assertNotNull(inMemoryGeoNameMaker.getGeoNameDao());

    assertNotNull(inMemoryStreetNodeMaker.getGeoNameDao());
    assertNotNull(inMemoryStreetNodeMaker.getStreetNodeDao());

    assertNotNull(streetMaker.getGeoNameDao());
    assertNotNull(streetMaker.getStreetNodeDao());
  }
}
