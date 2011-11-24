/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.tinelstudio.gis.model.SpringContexts;
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

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
@Transactional
public class HibernateGeoNameDaoManualTest {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private GeoNameDao geoNameDao;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Rollback
  @Test
  public void testNewGeoNameSave() {
    GeoName geoName=new GeoName();
    geoName.setName("CESTA");
    geoName.setType(Type.ADDRESS);

    geoNameDao.save(geoName);
  }

  @Transactional(readOnly=true)
  @Test
  public void testFindByName() {
    logger.info(findByName("CESTA"));
  }

  @Transactional(readOnly=true)
  @Test
  public void testFindByNames() {
    logger.info(findByNames("CeStA", "Ces"));
  }

  @Transactional(readOnly=true)
  @Test
  public void testFind() {
    logger.info(geoNameDao.load("CESTA", Type.ADDRESS));
  }

  @Transactional(readOnly=true)
  @SuppressWarnings("unchecked")
  public List<GeoName> findByName(String name) {
    return hibernateTemplate.find("from GeoName where lower(name) = ?", name
      .toLowerCase());
  }

  @Transactional(readOnly=true)
  @SuppressWarnings("unchecked")
  public List<GeoName> findByNames(String... names) {
    // Convert to lower case names
    Set<String> lowerNames=new HashSet<String>(names.length);
    for (String name : names) {
      lowerNames.add(name.toLowerCase());
    }
    return hibernateTemplate.findByNamedParam(
      "from GeoName where lower(name) in (:names)", "names", lowerNames);
  }
}