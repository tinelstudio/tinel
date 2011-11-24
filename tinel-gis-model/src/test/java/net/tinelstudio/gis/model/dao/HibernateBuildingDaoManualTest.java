/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.model.SpringContexts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
@Transactional
public class HibernateBuildingDaoManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private BuildingDao buildingDao;

  // private final GeometryFactory GF=new GeometryFactory(new PrecisionModel(),
  // 4326);

  @Rollback
  // @Rollback(false)
  @Test
  public void deleteAll() {
    logger.info("Deleting all buildings...");
    // buildingDao.delete(1192931L);
    buildingDao.deleteAll();
    logger.info("Committing transaction...");
  }
}