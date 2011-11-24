/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tinelstudio.gis.model.SpringContexts;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Coordinate;
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
@Transactional
public class HibernateAddressDaoManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private AddressDao addressDao;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  private final GeometryFactory GF=new GeometryFactory(new PrecisionModel(),
    4326);

  @Rollback
  @Test
  public void testNewAddressSave() {
    Address a=new Address();

    Coordinate c=new Coordinate(15, 46);
    Point point=GF.createPoint(c);
    a.setPoint(point);

    Set<GeoName> geoNames=new HashSet<GeoName>();
    GeoName geoName=new GeoName();
    geoName.setName("Name");
    geoName.setType(Type.ADDRESS);
    geoNames.add(geoName);
    a.setGeoNames(geoNames);

    String houseNumber="12A";
    a.setHouseNumber(houseNumber);

    String note="Note";
    a.setNote(note);

    addressDao.save(a);

    // Clear cache
    hibernateTemplate.flush();
    hibernateTemplate.clear();

    Long id=a.getId();
    assertNotNull(id);

    Address a2=addressDao.load(id);

    assertEquals(id, a2.getId());
    assertEquals(c, a2.getPoint().getCoordinate());
    assertEquals(houseNumber, a2.getHouseNumber());
    GeoName geoName2=(GeoName)a2.getGeoNames().toArray()[0];
    assertEquals(geoName.getName(), geoName2.getName());
    assertEquals(geoName.getType(), geoName2.getType());
    assertEquals(note, a2.getNote());

    // Clear cache
    hibernateTemplate.flush();
    hibernateTemplate.clear();

    addressDao.delete(id);

    // Clear cache
    hibernateTemplate.flush();
    hibernateTemplate.clear();

    Address a3=addressDao.load(id);
    assertNull(a3);
  }

  @Rollback
  @Test
  public void testDeleteAddresses() {
    addressDao.deleteAll();
    List<Address> as=addressDao.loadAll();
    assertTrue(as.isEmpty());
  }

  @Transactional(readOnly=true)
  // @Test
  public void testLoadAddresses() {
    List<Address> as=addressDao.loadAll();
    for (Address a : as) {
      logger.info(a);
    }
  }

}