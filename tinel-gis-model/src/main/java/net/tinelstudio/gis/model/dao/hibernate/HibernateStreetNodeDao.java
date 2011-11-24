/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao.hibernate;

import net.tinelstudio.gis.model.dao.StreetNodeDao;
import net.tinelstudio.gis.model.domain.StreetNode;

import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of {@link StreetNodeDao} using Hibernate.
 * 
 * @author TineL
 */
@Transactional
public class HibernateStreetNodeDao extends HibernateLoadAndSaveDao<StreetNode>
  implements StreetNodeDao {}
