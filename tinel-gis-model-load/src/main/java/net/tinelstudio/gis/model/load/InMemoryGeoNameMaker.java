/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.tinelstudio.gis.model.dao.GeoNameDao;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TineL
 */
@Component
public class InMemoryGeoNameMaker {

  protected final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private GeoNameDao geoNameDao;

  private Map<String, GeoName> geoNames;

  public GeoName findOrCreateGeoName(String name, Type type) {
    if (geoNames==null) {
      logger.info("Creating in-memory map of GeoNames...");
      geoNames=new Hashtable<String, GeoName>(1000000);
      preloadGeoNames(geoNames);
      logger.info("Done");
    }
    String key=generateGeoNameKey(name, type);
    GeoName existing=geoNames.get(key);
    if (existing==null) {
      existing=new GeoName();
      existing.setName(name);
      existing.setType(type);

      // Not need to persist GeoName now. It will be cascade persisted.

      geoNames.put(key, existing);
    }
    return existing;
  }

  private void preloadGeoNames(Map<String, GeoName> geoNames) {
    logger.info("Preloading all GeoNames from DB...");
    List<GeoName> names=getGeoNameDao().loadAll();
    for (GeoName name : names) {
      String key=generateGeoNameKey(name.getName(), name.getType());
      geoNames.put(key, name);
    }
    logger.info("Done. Preloaded ["+geoNames.size()+"] GeoNames");
  }

  private String generateGeoNameKey(String name, Type type) {
    return new StringBuilder(name).append("_").append(type.toString())
      .toString();
  }

  public int getGeoNamesCount() {
    return (geoNames==null)?0:geoNames.size();
  }

  public GeoNameDao getGeoNameDao() {
    return this.geoNameDao;
  }

  public void setGeoNameDao(GeoNameDao geoNameDao) {
    this.geoNameDao=geoNameDao;
  }
}