/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.SpringContexts;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
@Transactional
public class HibernateStreetDaoManualTest {

  @Autowired
  private StreetDao streetDao;

  private WKTReader wktReader=new WKTReader();

  // private final GeometryFactory GF=new GeometryFactory(new PrecisionModel(),
  // 4326);

  @Rollback
  @Test
  public void testNewStreetSave() throws Exception {
    LineString ls=(LineString)wktReader
      .read("LINESTRING(191232 243118,191108 243242)");
    ls.setSRID(4326);
    Street street=new Street();
    street.setLineString(ls);
    street.setOneWay(false);
    street.setLengthMeters(235);
    street.setLevel(Level.MEDIUM);
    StreetNode startNode=new StreetNode();
    Point startPoint=(Point)wktReader.read("POINT (191232 243118)");
    startPoint.setSRID(4326);
    startNode.setPoint(startPoint);
    street.setStartNode(startNode);
    StreetNode endNode=new StreetNode();
    Point endPoint=(Point)wktReader.read("POINT (191108 243242)");
    endPoint.setSRID(4326);
    endNode.setPoint(endPoint);
    street.setEndNode(endNode);

    streetDao.save(street);
  }

  @Rollback
  @Test
  @ExpectedException(DataIntegrityViolationException.class)
  public void testNewNullStreetSave() {
    Street street=new Street();

    streetDao.save(street);
  }

  @Rollback
  @Test
  public void testDeleteAllStreets() {
    streetDao.deleteAll();
  }
}