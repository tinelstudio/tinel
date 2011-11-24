/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import net.tinelstudio.gis.model.dao.LoadSaveDeleteDao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of the {@link LoadSaveDeleteDao} using Hibernate.
 * 
 * @author TineL
 * @param <E> the domain object
 */
@Transactional
public class HibernateLoadAndSaveDao<E> extends HibernateDaoSupport implements
  LoadSaveDeleteDao<E> {

  /** The known domain class instead of generic E. */
  private Class<E> domainClass;

  @SuppressWarnings("unchecked")
  public HibernateLoadAndSaveDao() {
    // Trick: Get known domain class from generic
    this.domainClass=(Class<E>)((ParameterizedType)getClass()
      .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Transactional(readOnly=true)
  @Override
  @SuppressWarnings("unchecked")
  public E load(Long id) {
    return (E)getHibernateTemplate().get(this.domainClass, id);
  }

  @Transactional(readOnly=true)
  @Override
  @SuppressWarnings("unchecked")
  public List<E> loadAll() {
    return getHibernateTemplate().loadAll(this.domainClass);
  }

  @Override
  public void save(E element) {
    getHibernateTemplate().saveOrUpdate(element);
  }

  @Override
  public void saveAll(Collection<E> elements) {
    getHibernateTemplate().saveOrUpdateAll(elements);
  }

  @Override
  public boolean delete(Long id) {
    E element=load(id);
    if (element==null) return false;
    getHibernateTemplate().delete(element);
    return true;
  }

  @Override
  public void deleteAll() {
    getHibernateTemplate().deleteAll(loadAll());
  }
}
