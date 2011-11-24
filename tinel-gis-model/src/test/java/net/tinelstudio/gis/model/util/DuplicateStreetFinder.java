/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.util;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.model.SpringContexts;
import net.tinelstudio.gis.model.dao.StreetDao;
import net.tinelstudio.gis.model.domain.Street;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * A utility to find duplicate streets in DB.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
public class DuplicateStreetFinder {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private StreetDao streetDao;

  @Transactional(readOnly=true)
  @Test
  public void findDuplicateStreets() {
    logger.info("Loading all streets from DB...");
    List<Street> streets=streetDao.loadAll();

    logger.info("Searching for duplicate streets ["+streets.size()+"]...");

    // Difference between operator = (equals) and ~= (equalsExact):
    // Operator ~= does not equal geometries that are reversed, i.e. streets:
    // LINESTRING (14 45, 15 46) = LINESTRING (15 46, 14 45) == true
    // LINESTRING (14 45, 15 46) ~= LINESTRING (15 46, 14 45) == false
    // Operator ~= is much faster.

    // SELECT a.*, b.* FROM street a, street b
    // WHERE a.lineString = b.lineString AND a.id <> b.id

    List<Street> duplicateStreets=new ArrayList<Street>();
    for (Street a : streets) {
      for (Street b : streets) {
        if (a==b) continue;
        if (a.getLineString().equals(b.getLineString())
          &&!duplicateStreets.contains(a)) {
          logger.info("Found duplicates. Try to delete "+a+" or "+b);
          duplicateStreets.add(b);
        }
      }
    }

    logger.info("Try to manually delete duplicates with "
      +StreetDeleter.class.getSimpleName());
  }
}