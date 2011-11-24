/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.util;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.model.old.SpringContexts;
import net.tinelstudio.gis.model.old.dao.OldStreetDao;
import net.tinelstudio.gis.model.old.domain.Eazvceste_fixed;
import net.tinelstudio.gis.model.old.domain.Ebzsceste2_fixed;
import net.tinelstudio.gis.model.old.domain.Eczmceste_fixed;
import net.tinelstudio.gis.model.old.domain.OldStreet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * A utility to find duplicate old streets in DB.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={SpringContexts.OLD_DB_CONTEXT})
@TransactionConfiguration(transactionManager="oldTxManager")
public class DuplicateOldStreetFinder {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private OldStreetDao oldStreetDao;

  @Transactional(readOnly=true)
  @Test
  public void start() {
    findDuplicateStreets(Eazvceste_fixed.class);
    findDuplicateStreets(Ebzsceste2_fixed.class);
    findDuplicateStreets(Eczmceste_fixed.class);
  }

  private void findDuplicateStreets(
    Class<? extends OldStreet> oldStreetEntityClass) {
    logger.info("Loading all streets from DB...");
    List<? extends OldStreet> streets=oldStreetDao
      .loadAll(oldStreetEntityClass);

    logger.info("Searching for duplicate streets ["+streets.size()+"]...");

    // Difference between operator = and ~=:
    // Operator ~= does not equal geometries that are reversed, i.e. streets:
    // LINESTRING (14 45, 15 46) = LINESTRING (15 46, 14 45) == true
    // LINESTRING (14 45, 15 46) ~= LINESTRING (15 46, 14 45) == false
    // Operator ~= is much faster.
    //    
    // SELECT a.*, b.* FROM eczmceste a, eczmceste b
    // WHERE a.geom = b.geom AND a.gid <> b.gid

    List<OldStreet> duplicateStreets=new ArrayList<OldStreet>();
    for (OldStreet a : streets) {
      for (OldStreet b : streets) {
        if (a==b) continue;
        if (a.getGeom().equals(b.getGeom())&&!duplicateStreets.contains(a)) {
          logger.info("Found duplicates. Try to delete "+a+" or "+b);
          duplicateStreets.add(b);
        }
      }
    }

    logger.info("Try to manually delete duplicates with "
      +OldStreetDeleter.class.getSimpleName());
  }
}