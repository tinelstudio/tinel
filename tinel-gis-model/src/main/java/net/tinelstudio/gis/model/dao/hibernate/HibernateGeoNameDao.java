/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao.hibernate;

import java.util.List;

import net.tinelstudio.gis.model.dao.GeoNameDao;
import net.tinelstudio.gis.model.domain.GeoName;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * The implementation of {@link GeoNameDao} using Hibernate.
 * 
 * @author TineL
 */
@Transactional
public class HibernateGeoNameDao extends HibernateLoadAndSaveDao<GeoName>
  implements GeoNameDao {

  @Transactional(readOnly=true)
  @Override
  public GeoName load(String name, Type type) {
    @SuppressWarnings("unchecked")
    List<GeoName> names=getHibernateTemplate().find(
      "from GeoName where lower(name) = ? and type = ?",
      new Object[]{name.toLowerCase(), type});

    if (names.isEmpty()) return null;

    // Name & type are considered unique = no duplicates!
    Assert.isTrue(names.size()<=1, "GeoName duplicates!");

    return names.get(0);
  }
}
