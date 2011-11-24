/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.model.SpringContexts;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.CustomType;
import org.hibernatespatial.GeometryUserType;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

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
@Transactional(readOnly=true)
public class HibernateFindingDaoManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private FindingDao dao;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  private final GeometryFactory GF=new GeometryFactory(new PrecisionModel(),
    4326);

  private static final int REPEATS=1;

  // @Test
  @Repeat(REPEATS)
  public void testHibernateSpeed() throws Exception {
    @SuppressWarnings("unchecked")
    List<GeoName> geoNames=hibernateTemplate
      .find("from GeoName where lower(name) like '%cank%' and type='"
        +Type.ADDRESS+"'");

    logger.info("Hibernate hits: "+geoNames.size());
    if (!geoNames.isEmpty()) {
      for (int i=0; i<geoNames.size(); i++) {
        logger.info("Hit #"+(i+1)+": "+geoNames.get(i));
      }
    }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestAddresses() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Address> addresses=dao.findNearestAddresses(point, 5000, 1000);
    logger.info("Found addresses ["+addresses.size()+"]:");
    // for (Address a : addresses) {
    // logger.info(a);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestAddressesSlow() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Address> addresses=findNearestAddressesSlow(point, 35000, 10);
    logger.info("Found addresses ["+addresses.size()+"]:");
    // for (Address a : addresses) {
    // logger.info(a);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestAddressesApprox() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Address> addresses=dao.findNearestAddressesApprox(point, 50000, 10);
    logger.info("Found addresses ["+addresses.size()+"]:");
    // for (Address a : addresses) {
    // logger.info(a);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestAddressesUtm() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Address> addresses=findNearestAddressesUtm(point, 5000, 10);
    logger.info("Found addresses ["+addresses.size()+"]:");
    // for (Address a : addresses) {
    // logger.info(a);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestStreets() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Street> streets=dao.findNearestStreets(point, 5000, 10);
    logger.info("Found streets ["+streets.size()+"]:");
    // for (Street s : streets) {
    // logger.info(s);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestStreetsApprox() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Street> streets=dao.findNearestStreetsApprox(point, 1000, 10);
    logger.info("Found streets ["+streets.size()+"]:");
    // for (Street s : streets) {
    // logger.info(s);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestBuildings() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Building> buildings=dao.findNearestBuildings(point, 5000, 10);
    logger.info("Found buildings ["+buildings.size()+"]:");
    // for (Building b : buildings) {
    // logger.info(b);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestBuildingsApprox() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    List<Building> buildings=dao.findNearestBuildingsApprox(point, 1000, 10);
    logger.info("Found buildings ["+buildings.size()+"]:");
    // for (Building b : buildings) {
    // logger.info(b);
    // }
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestStreetNode() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    StreetNode streetNode=dao.findNearestStreetNode(point, 1000);
    logger.info("Found street node : "+streetNode.getPoint());
  }

  // @Test
  @Repeat(REPEATS)
  public void testFindNearestStreetNodeApprox() {
    Point point=GF.createPoint(new Coordinate(15, 46));
    StreetNode streetNode=dao.findNearestStreetNodeApprox(point, 1000);
    logger.info("Found street node : "+streetNode.getPoint());
  }

  private List<Address> findNearestAddressesUtm(final Geometry place,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    final int srid=32633; // WGS 84 / Transverse Mercator for UTM zone 33 North

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Address> addresses=hibernateTemplate
      .executeFind(new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Address as a where "
            +"ST_DWithin(ST_Transform(a.point, :utm), "
            +"ST_Transform(:place, :utm), :distance) = true "
            +"order by ST_Distance(ST_Transform(a.point, :utm), "
            +"ST_Transform(:place, :utm))");
          // Add place as explicit geometry
          query.setParameter("place", place, geometryType);
          // Add search distance
          query.setDouble("distance", maxDistanceMeters);
          // Add UTM SRID
          query.setInteger("utm", srid);

          if (maxResults>0) {
            // Return only nearest
            query.setMaxResults(maxResults);
          }
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Addresses ["+addresses.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return addresses;
  }

  private List<Address> findNearestAddressesSlow(final Point point,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Address> addresses=hibernateTemplate
      .executeFind(new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query q=session.createQuery("from Address as a where "
            +"ST_distance_sphere(a.point, :point) <= :distance "
            +"order by ST_distance_sphere(a.point, :point)");
          // Add point as explicit geometry
          q.setParameter("point", point, geometryType);
          // Add search distance
          q.setInteger("distance", maxDistanceMeters);

          if (maxResults>0) {
            // Return only nearest
            q.setMaxResults(maxResults);
          }
          return q.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Addresses ["+addresses.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return addresses;
  }

  // @Test
  @Repeat(REPEATS)
  public void testFind() {
    List<String> names=new ArrayList<String>();
    names.add("Cank");
    // names.add("ul");
    List<String> houseNumbers=new ArrayList<String>();
    houseNumbers.add("1");

    List<Address> res=this.dao.findAddresses(names, houseNumbers, 100);
    // List<Street> res=this.dao.findStreets(names, 100);
    logger.info("Found ["+res.size()+"]:");
    for (Address r : res) {
      // for (Street r : res) {
      logger.info(r);
    }
  }
}
