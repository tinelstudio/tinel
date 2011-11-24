/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.domain.Continent;
import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.dao.StreetDao;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.GeoName.Type;
import net.tinelstudio.gis.model.load.StreetMaker;
import net.tinelstudio.gis.model.old.SpringContexts;
import net.tinelstudio.gis.model.old.dao.OldStreetDao;
import net.tinelstudio.gis.model.old.domain.Eazvceste_fixed;
import net.tinelstudio.gis.model.old.domain.Ebzsceste2_fixed;
import net.tinelstudio.gis.model.old.domain.Eczmceste_fixed;
import net.tinelstudio.gis.model.old.domain.OldStreet;

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

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * All new streets: 49171. Run with JVM argument -Xmx256M min. Run time for all
 * streets to local DB without town findings: ~2 min.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
  net.tinelstudio.gis.model.SpringContexts.DB_CONTEXT,
  net.tinelstudio.gis.model.load.SpringContexts.LOAD_CONTEXT,
  SpringContexts.OLD_DB_CONTEXT})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
@Transactional
public class OldStreetTranslator extends StreetMaker {

  private final String defaultCountry="Slovenija";
  private final String defaultContinent=Continent.EUROPE;

  private final boolean searchForTowns=false;
  private final int nearbyTownMaxDistance=1000; // In meters

  @Autowired
  private OldStreetDao oldStreetDao;

  @Autowired
  private StreetDao streetDao;

  @Autowired
  private FindingDao findingDao;

  // @Rollback
  @Rollback(false)
  @Test
  public void start() {
    translate(Eazvceste_fixed.class, Level.MAJOR);
    translate(Ebzsceste2_fixed.class, Level.MEDIUM);
    translate(Eczmceste_fixed.class, Level.MINOR);
    logger.info("Committing transaction...");
  }

  private void translate(Class<? extends OldStreet> oldStreetClass, Level level) {
    logger.info("Obtaining old streets '"+oldStreetClass+"' from DB...");
    List<? extends OldStreet> oldStreets=oldStreetDao.loadAll(oldStreetClass);

    String note="Source:"+oldStreetClass.getSimpleName();

    logger.info("Converting old streets ["+oldStreets.size()+"]...");
    List<Street> newStreets=new ArrayList<Street>(oldStreets.size()*2);
    int n=0;
    for (OldStreet oldStreet : oldStreets) {
      n++;

      // Get shape
      LineString ls=oldStreet.getGeom();

      String town=null;
      if (searchForTowns) {
        // Find nearest town
        List<Address> as=findingDao.findNearestAddressesApprox(ls,
          nearbyTownMaxDistance, 1);
        if (!as.isEmpty()) {
          Address a=as.get(0);
          Set<GeoName> geoNames=a.getGeoNames();
          for (GeoName geoName : geoNames) {
            if (geoName.getType()==Type.TOWN) {
              town=geoName.getName();
              break;
            }
          }
        }
      }

      String name=oldStreet.getLabel();
      if (name==null) {
        logger.info("Old street does not have name: "+oldStreet);
      }

      boolean oneWay=oldStreet.getOne_way();

      int lengthMeters=computeLengthMeters(ls);

      Point startPoint=ls.getStartPoint();
      startPoint.setSRID(ls.getSRID());
      Point endPoint=ls.getEndPoint();
      endPoint.setSRID(ls.getSRID());

      Street r=createStreet(ls, defaultContinent, defaultCountry, null, town,
        name, oneWay, lengthMeters, level, note, startPoint, endPoint);

      if (n%1000==0) {
        logger.info("New street #"+n+": "+r.getGeoNames());
      }

      newStreets.add(r);
    }

    logger.info("oldStreets="+oldStreets.size()+", newStreets="
      +newStreets.size());

    // logger.info("Deleting all current streets from DB...");
    // streetDao.deleteAll();

    logger.info("Saving new streets to DB...");
    streetDao.saveAll(newStreets);
  }
}