/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.dao.hibernate;

import java.util.List;

import net.tinelstudio.gis.model.old.dao.OldStreetDao;
import net.tinelstudio.gis.model.old.domain.OldStreet;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of the {@link OldStreetDao} using Hibernate.
 * 
 * @author TineL
 */
@Transactional
public class HibernateOldStreetDao extends HibernateDaoSupport implements
  OldStreetDao {

  @Transactional(readOnly=true)
  @Override
  @SuppressWarnings("unchecked")
  public List<? extends OldStreet> loadAll(
    Class<? extends OldStreet> oldEntityClass) {
    return getHibernateTemplate().loadAll(oldEntityClass);
  }

  @Override
  public boolean delete(Class<? extends OldStreet> oldEntityClass, Long id) {
    Object element=getHibernateTemplate().get(oldEntityClass, id);
    if (element==null) return false;
    getHibernateTemplate().delete(element);
    return true;
  }
}
