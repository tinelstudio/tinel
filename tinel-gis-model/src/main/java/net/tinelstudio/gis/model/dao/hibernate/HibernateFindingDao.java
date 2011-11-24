/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.commons.DistanceOnEarth;
import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.Building;
import net.tinelstudio.gis.model.domain.GeoName.Type;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.CustomType;
import org.hibernatespatial.GeometryUserType;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * The implementation of {@link FindingDao} using Hibernate.
 * 
 * @author TineL
 */
@Transactional(readOnly=true)
public class HibernateFindingDao extends HibernateDaoSupport implements
  FindingDao {

  private final GeometryFactory GF=new GeometryFactory(new PrecisionModel(),
    4326);

  // #################
  // Findings by names

  @Override
  public List<Address> findAddresses(List<String> names,
    List<String> houseNumbers, int maxResults) {

    // Query parameters
    List<Object> params=new ArrayList<Object>();

    String query="select a from Address as a ";

    String houseQuery="";
    if (houseNumbers!=null) {
      for (int i=0; i<houseNumbers.size(); i++) {
        houseQuery+="lower(a.houseNumber) like ? ";
        if (i<houseNumbers.size()-1) {
          houseQuery+="and ";
        }
        params.add(houseNumbers.get(i).toLowerCase()+"%");
      }
    }

    String namesQuery="";
    if (names!=null&&!names.isEmpty()) {
      query+="join a.geoNames as geoname ";

      String geoNameQuery="";
      for (int i=0; i<names.size(); i++) {
        geoNameQuery+="lower(geoname.name) like ? ";
        if (i<names.size()-1) {
          geoNameQuery+="and ";
        }
        params.add("%"+names.get(i).toLowerCase()+"%");
      }

      namesQuery+=geoNameQuery+" and geoname.type=? ";
      params.add(Type.ADDRESS);
    }

    if (!houseQuery.isEmpty()) {
      query+="where ("+houseQuery+") ";
    }
    if (!namesQuery.isEmpty()) {
      query+=houseQuery.isEmpty()?"where ":"and ";
      query+=namesQuery;
    }

    StopWatch sw=new StopWatch();
    sw.start();

    getHibernateTemplate().setMaxResults(maxResults);
    @SuppressWarnings("unchecked")
    List<Address> addresses=getHibernateTemplate()
      .find(query, params.toArray());

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Addresses ["+addresses.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return addresses;
  }

  @Override
  public List<Address> findDistinctAddressesByType(List<String> names,
    Type type, int maxResults) {
    switch (type) {
    case ADDRESS:
    case TOWN:
    case REGION:
    case COUNTRY:
    case CONTINENT: {
      // Query parameters
      List<Object> params=new ArrayList<Object>();

      String geoNameQuery="";
      for (int i=0; i<names.size(); i++) {
        geoNameQuery+="lower(geoname.name) like ? ";
        if (i<names.size()-1) {
          geoNameQuery+="and ";
        }
        params.add("%"+names.get(i).toLowerCase()+"%");
      }

      /*
       * Special case: Find only one address (that with smallest ID = min(a))
       * per distinctive name.
       */
      String query="from Address as a where a in (select min(a) "
        +"from Address as a join a.geoNames as geoname where ("+geoNameQuery
        +") and geoname.type=? group by geoname.name)";
      params.add(type);

      StopWatch sw=new StopWatch();
      sw.start();

      getHibernateTemplate().setMaxResults(maxResults);
      @SuppressWarnings("unchecked")
      List<Address> addresses=getHibernateTemplate().find(query,
        params.toArray());

      sw.stop();
      if (logger.isDebugEnabled()) {
        logger.debug("Found Addresses ["+addresses.size()+"] in "
          +sw.getLastTaskTimeMillis()+" ms");
      }

      return addresses;
    }
    default:
      throw new IllegalArgumentException("Type can only be "+Type.ADDRESS+", "
        +Type.TOWN+", "+Type.REGION+", "+Type.COUNTRY+" or "+Type.CONTINENT);
    }
  }

  @Override
  public List<Street> findStreets(List<String> names, int maxResults) {
    // Query parameters
    List<Object> params=new ArrayList<Object>();

    String geoNameQuery="";
    for (int i=0; i<names.size(); i++) {
      geoNameQuery+="lower(geoname.name) like ? ";
      if (i<names.size()-1) {
        geoNameQuery+="and ";
      }
      params.add("%"+names.get(i).toLowerCase()+"%");
    }

    String query="select s from Street as s join s.geoNames as geoname where ("
      +geoNameQuery+") and geoname.type=?";
    params.add(Type.STREET);

    StopWatch sw=new StopWatch();
    sw.start();

    getHibernateTemplate().setMaxResults(maxResults);
    @SuppressWarnings("unchecked")
    List<Street> streets=getHibernateTemplate().find(query, params.toArray());

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Streets ["+streets.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return streets;
  }

  @Override
  public List<Street> findDistinctStreetsByType(List<String> names, Type type,
    int maxResults) {
    switch (type) {
    case TOWN:
    case REGION:
    case COUNTRY:
    case CONTINENT: {
      // Query parameters
      List<Object> params=new ArrayList<Object>();

      String geoNameQuery="";
      for (int i=0; i<names.size(); i++) {
        geoNameQuery+="lower(geoname.name) like ? ";
        if (i<names.size()-1) {
          geoNameQuery+="and ";
        }
        params.add("%"+names.get(i).toLowerCase()+"%");
      }

      /*
       * Special case: Find only one street (that with smallest ID = min(s)) per
       * distinctive name.
       */
      String query="from Street as s where s in (select min(s) "
        +"from Street as s join s.geoNames as geoname where ("+geoNameQuery
        +") and geoname.type=? group by geoname.name)";
      params.add(type);

      StopWatch sw=new StopWatch();
      sw.start();

      getHibernateTemplate().setMaxResults(maxResults);
      @SuppressWarnings("unchecked")
      List<Street> streets=getHibernateTemplate().find(query, params.toArray());

      sw.stop();
      if (logger.isDebugEnabled()) {
        logger.debug("Found Streets ["+streets.size()+"] in "
          +sw.getLastTaskTimeMillis()+" ms");
      }

      return streets;
    }
    default:
      throw new IllegalArgumentException("Type can only be "+Type.TOWN+", "
        +Type.REGION+", "+Type.COUNTRY+" or "+Type.CONTINENT);
    }
  }

  @Override
  public List<Building> findBuildings(List<String> names, int maxResults) {
    // Query parameters
    List<Object> params=new ArrayList<Object>();

    String geoNameQuery="";
    for (int i=0; i<names.size(); i++) {
      geoNameQuery+="lower(geoname.name) like ? ";
      if (i<names.size()-1) {
        geoNameQuery+="and ";
      }
      params.add("%"+names.get(i).toLowerCase()+"%");
    }

    String q="select b from Building as b join b.geoNames as geoname "
      +"where ("+geoNameQuery+") and geoname.type=?";
    params.add(Type.BUILDING);

    StopWatch sw=new StopWatch();
    sw.start();

    getHibernateTemplate().setMaxResults(maxResults);
    @SuppressWarnings("unchecked")
    List<Building> buildings=getHibernateTemplate().find(q, params.toArray());

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Buildings ["+buildings.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return buildings;
  }

  @Override
  public List<Building> findDistinctBuildingsByType(List<String> names,
    Type type, int maxResults) {
    switch (type) {
    case TOWN:
    case REGION:
    case COUNTRY:
    case CONTINENT: {
      // Query parameters
      List<Object> params=new ArrayList<Object>();

      String geoNameQuery="";
      for (int i=0; i<names.size(); i++) {
        geoNameQuery+="lower(geoname.name) like ? ";
        if (i<names.size()-1) {
          geoNameQuery+="and ";
        }
        params.add("%"+names.get(i).toLowerCase()+"%");
      }

      /*
       * Special case: Find only one building (that with smallest ID = min(b))
       * per distinctive name.
       */
      String query="from Building as b where b in (select min(b) "
        +"from Building as b join b.geoNames as geoname where ("+geoNameQuery
        +") and geoname.type=? group by geoname.name)";
      params.add(type);

      StopWatch sw=new StopWatch();
      sw.start();

      getHibernateTemplate().setMaxResults(maxResults);
      @SuppressWarnings("unchecked")
      List<Building> buildings=getHibernateTemplate().find(query,
        params.toArray());

      sw.stop();
      if (logger.isDebugEnabled()) {
        logger.debug("Found Buildings ["+buildings.size()+"] in "
          +sw.getLastTaskTimeMillis()+" ms");
      }

      return buildings;
    }
    default:
      throw new IllegalArgumentException("Type can only be "+Type.TOWN+", "
        +Type.REGION+", "+Type.COUNTRY+" or "+Type.CONTINENT);
    }
  }

  // ################
  // Nearest findings

  /**
   * Computes a bounding box around the given geometry that spans for the given
   * distance in all directions from the given geometry. The given geometry must
   * have coordinates in degrees (SRID must be 4326). The computed bounding box
   * has also coordinates in degrees (SRID is 4326). If the given distance is 0,
   * the given geometry is returned.
   * 
   * @param geometry the geometry to compute bounding box around (
   *        <code>null</code> not permitted)
   * @param distanceMeters the distance to span in meters (must be >= 0)
   * @return a bounding box (<code>null</code> not possible)
   * @throws IllegalArgumentException If geometry's SRID is not 4326 or if
   *         distanceMeters exceeds some limitations
   * @see DistanceOnEarth
   */
  protected Geometry computeBoundingBox(Geometry geometry, int distanceMeters) {
    Assert.isTrue(geometry.getSRID()==4326, "Geometry's SRID must be 4326");

    if (distanceMeters==0) return geometry;

    Envelope envelope=geometry.getEnvelopeInternal();
    double minX=envelope.getMinX();
    double maxX=envelope.getMaxX();
    double minY=envelope.getMinY();
    double maxY=envelope.getMaxY();

    // Span south
    double newMinY=DistanceOnEarth.latitudeAtDistance(minY, distanceMeters);
    // Span north
    double newMaxY=DistanceOnEarth.latitudeAtDistance(maxY, -distanceMeters);
    // Span east
    double newMaxX=DistanceOnEarth.longitudeAtDistance(maxX, distanceMeters,
      minY);
    // Span west
    double newMinX=DistanceOnEarth.longitudeAtDistance(minX, -distanceMeters,
      maxY);

    Envelope bbox=new Envelope(newMinX, newMaxX, newMinY, newMaxY);

    return GF.toGeometry(bbox);
  }

  @Override
  public List<Address> findNearestAddresses(final Point point,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    /*
     * Add bounding box search to cut down ST_distance_sphere calculations and
     * to significantly speed up query
     */
    final Geometry bbox=computeBoundingBox(point, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Address> addresses=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Address as a "
            +"where ST_Intersects(a.point, :bbox) = true and "
            +"ST_distance_sphere(a.point, :point) <= :distance "
            +"order by ST_distance_sphere(a.point, :point)");
          // Add point as explicit geometry
          query.setParameter("point", point, geometryType);
          // Add search distance
          query.setInteger("distance", maxDistanceMeters);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);

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

  @Override
  public StreetNode findNearestStreetNode(final Point point,
    final int maxDistanceMeters) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    /*
     * Add bounding box search to cut down ST_distance_sphere calculations and
     * to significantly speed up query
     */
    final Geometry bbox=computeBoundingBox(point, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<StreetNode> streetNodes=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from StreetNode as sn "
            +"where ST_Intersects(sn.point, :bbox) = true "
            +"and ST_distance_sphere(sn.point, :point) <= :distance "
            +"order by ST_distance_sphere(sn.point, :point)");
          // Add point as explicit geometry
          query.setParameter("point", point, geometryType);
          // Add search distance
          query.setInteger("distance", maxDistanceMeters);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);
          // Return only the nearest
          query.setMaxResults(1);
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found StreetNode in "+sw.getLastTaskTimeMillis()+" ms");
    }

    if (streetNodes.isEmpty()) return null;
    return streetNodes.get(0);
  }

  @Override
  public List<Street> findNearestStreets(final Point point,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    /*
     * Add bounding box search to cut down ST_distance_sphere calculations and
     * to significantly speed up query
     */
    final Geometry bbox=computeBoundingBox(point, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Street> streets=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Street as s where "
            +"ST_Intersects(s.lineString, :bbox) = true "
            +"and ST_distance_sphere(:point, "
            +"ST_line_interpolate_point(s.lineString, "
            +"ST_line_locate_point(s.lineString, :point))) <= :distance "
            +"order by ST_distance_sphere(:point, "
            +"ST_line_interpolate_point(s.lineString, "
            +"ST_line_locate_point(s.lineString, :point)))");
          // Add point as explicit geometry
          query.setParameter("point", point, geometryType);
          // Add search distance
          query.setInteger("distance", maxDistanceMeters);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);

          if (maxResults>0) {
            // Return only nearest
            query.setMaxResults(maxResults);
          }
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Streets ["+streets.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return streets;
  }

  @Override
  public List<Building> findNearestBuildings(final Point point,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    /*
     * Add bounding box search to cut down ST_distance_sphere calculations and
     * to significantly speed up query
     */
    final Geometry bbox=computeBoundingBox(point, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Building> buildings=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Building as b where "
            +"ST_Intersects(b.polygon, :bbox) = true "
            +"and ST_distance_sphere(:point, "
            +"ST_line_interpolate_point(ST_ExteriorRing(b.polygon), "
            +"ST_line_locate_point(ST_ExteriorRing(b.polygon), :point))) "
            +"<= :distance order by ST_distance_sphere(:point, "
            +"ST_line_interpolate_point(ST_ExteriorRing(b.polygon), "
            +"ST_line_locate_point(ST_ExteriorRing(b.polygon), :point)))");
          // Add point as explicit geometry
          query.setParameter("point", point, geometryType);
          // Add search distance
          query.setInteger("distance", maxDistanceMeters);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);

          if (maxResults>0) {
            // Return only nearest
            query.setMaxResults(maxResults);
          }
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Buildings ["+buildings.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return buildings;
  }

  @Override
  public List<Address> findNearestAddressesApprox(final Geometry place,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    final Geometry bbox=computeBoundingBox(place, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Address> addresses=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Address as a where "
            +"ST_Intersects(a.point, :bbox) = true "
            +"order by ST_Distance(a.point, :place)");
          // Add place as explicit geometry
          query.setParameter("place", place, geometryType);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);

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

  @Override
  public StreetNode findNearestStreetNodeApprox(final Geometry place,
    final int maxDistanceMeters) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    final Geometry bbox=computeBoundingBox(place, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<StreetNode> streetNodes=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from StreetNode as sn where "
            +"ST_Intersects(sn.point, :bbox) = true "
            +"order by ST_Distance(sn.point, :place)");
          // Add place as explicit geometry
          query.setParameter("place", place, geometryType);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);
          // Return only the nearest
          query.setMaxResults(1);
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found StreetNode in "+sw.getLastTaskTimeMillis()+" ms");
    }

    if (streetNodes.isEmpty()) return null;
    return streetNodes.get(0);
  }

  @Override
  public List<Street> findNearestStreetsApprox(final Geometry place,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    final Geometry bbox=computeBoundingBox(place, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Street> streets=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query query=session.createQuery("from Street as s where "
            +"ST_Intersects(s.lineString, :bbox) = true "
            +"order by ST_Distance(s.lineString, :place)");
          // Add place as explicit geometry
          query.setParameter("place", place, geometryType);
          // Add search bounding box as explicit geometry
          query.setParameter("bbox", bbox, geometryType);

          if (maxResults>0) {
            // Return only nearest
            query.setMaxResults(maxResults);
          }
          return query.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Streets ["+streets.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return streets;
  }

  @Override
  public List<Building> findNearestBuildingsApprox(final Geometry place,
    final int maxDistanceMeters, final int maxResults) {
    Assert.isTrue(maxDistanceMeters>=0, "maxDistanceMeters must be >= 0");

    final Geometry bbox=computeBoundingBox(place, maxDistanceMeters);

    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Building> buildings=getHibernateTemplate().executeFind(
      new HibernateCallback() {
        @Override
        public Object doInHibernate(Session session) throws HibernateException,
          SQLException {
          // Explicit geometry type
          CustomType geometryType=new CustomType(GeometryUserType.class, null);
          // HQL
          Query q=session.createQuery("from Building as b where "
            +"ST_Intersects(b.polygon, :bbox) = true "
            +"order by ST_Distance(b.polygon, :place)");
          // Add place as explicit geometry
          q.setParameter("place", place, geometryType);
          // Add search bounding box as explicit geometry
          q.setParameter("bbox", bbox, geometryType);

          if (maxResults>0) {
            // Return only nearest
            q.setMaxResults(maxResults);
          }
          return q.list();
        }
      });

    sw.stop();
    if (logger.isDebugEnabled()) {
      logger.debug("Found Buildings ["+buildings.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return buildings;
  }

  // ######################
  // Miscellaneous findings

  @Override
  public List<Street> findConnectedStreets(StreetNode streetNode) {
    StopWatch sw=new StopWatch();
    sw.start();

    @SuppressWarnings("unchecked")
    List<Street> streets=getHibernateTemplate().find(
      "from Street where startNode = ? or (oneWay = ? and endNode = ?)",
      new Object[]{streetNode, false, streetNode});

    sw.stop();
    if (logger.isTraceEnabled()) {
      logger.trace("Found Streets ["+streets.size()+"] in "
        +sw.getLastTaskTimeMillis()+" ms");
    }

    return streets;
  }
}
