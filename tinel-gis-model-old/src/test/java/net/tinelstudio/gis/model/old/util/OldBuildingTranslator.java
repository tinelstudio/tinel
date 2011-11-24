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
import net.tinelstudio.gis.model.dao.BuildingDao;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;
import net.tinelstudio.gis.model.load.BuildingMaker;
import net.tinelstudio.gis.model.old.SpringContexts;
import net.tinelstudio.gis.model.old.dao.GczzgradbeDao;
import net.tinelstudio.gis.model.old.domain.Gczzgradbe;

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

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * New buildings: 876238. Run with JVM argument -Xmx1536M min. Run time to local
 * DB without town findings: ~25 min.
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
public class OldBuildingTranslator extends BuildingMaker {

  private final String defaultCountry="Slovenija";
  private final String defaultContinent=Continent.EUROPE;

  private final boolean searchForTowns=false;
  private final int nearbyTownMaxDistance=1000; // In meters

  @Autowired
  private GczzgradbeDao gczzgradbeDao;

  @Autowired
  private BuildingDao buildingDao;

  @Autowired
  private FindingDao findingDao;

  // @Rollback
  @Rollback(false)
  @Test
  public void start() {
    logger.info("Obtaining old buildings from DB...");
    List<Gczzgradbe> oldBuildings=gczzgradbeDao.loadAll();

    String note="Source:"+Gczzgradbe.class.getSimpleName();

    logger.info("Converting old buildings ["+oldBuildings.size()+"]...");
    List<Building> newBuildings=new ArrayList<Building>(oldBuildings.size()*2);
    int n=0;
    for (Gczzgradbe oldBuilding : oldBuildings) {
      MultiPolygon mpg=oldBuilding.getGeom();

      // Convert one MULTIPOLYGON to more POLYGONs
      for (int i=0; i<mpg.getNumGeometries(); i++) {
        n++;
        // Get shape
        Polygon polygon=(Polygon)mpg.getGeometryN(i);
        polygon.setSRID(mpg.getSRID());

        // Find nearest town
        String town=null;
        if (searchForTowns) {
          List<Address> as=findingDao.findNearestAddressesApprox(polygon,
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

        String name=oldBuilding.getLabel();

        Building b=createBuilding(polygon, defaultContinent, defaultCountry,
          null, town, name, note);

        if (n%1000==0) {
          logger.info("New building #"+n+" from old gid="+oldBuilding.getGid());
        }

        newBuildings.add(b);
      }
    }

    logger.info("oldBuildings="+oldBuildings.size()+", newBuildings="
      +newBuildings.size());

    // logger.info("Deleting all current buildings from DB...");
    // buildingDao.deleteAll();

    logger.info("Saving new buildings to DB...");
    buildingDao.saveAll(newBuildings);

    logger.info("Committing transaction...");
  }
}