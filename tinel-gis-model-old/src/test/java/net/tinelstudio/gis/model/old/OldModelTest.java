/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old;

import net.tinelstudio.gis.model.old.dao.OldStreetDao;
import net.tinelstudio.gis.model.old.domain.Eazvceste_fixed;
import net.tinelstudio.gis.model.old.domain.Ebzsceste2_fixed;
import net.tinelstudio.gis.model.old.domain.Eczmceste_fixed;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.OLD_DB_CONTEXT)
@TransactionConfiguration(transactionManager="oldTxManager")
public class OldModelTest {

  @Autowired
  private OldStreetDao oldDao;

  @Transactional
  @Rollback
  @Test
  public void testIntegrity() {
    oldDao.delete(Eazvceste_fixed.class, 1L);
    oldDao.delete(Ebzsceste2_fixed.class, 1L);
    oldDao.delete(Eczmceste_fixed.class, 1L);
  }
}