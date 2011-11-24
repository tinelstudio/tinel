/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.util;

import net.tinelstudio.gis.model.SpringContexts;
import net.tinelstudio.gis.model.dao.StreetDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A utility to manually delete streets from DB.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
@Transactional
public class StreetDeleter {

  private final long[] DELETE_STREETS_ID={};

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private StreetDao streetDao;

  @Rollback(false)
  @Test
  public void deleteStreets() {
    logger.info("Deleting streets...");
    for (long id : DELETE_STREETS_ID) {
      streetDao.delete(id);
      logger.info("Street id="+id+" deleted");
    }
  }
}