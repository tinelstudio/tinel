/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.dao.hibernate;

import net.tinelstudio.gis.model.dao.BuildingDao;
import net.tinelstudio.gis.model.domain.Building;

import org.springframework.transaction.annotation.Transactional;

/**
 * The implementation of {@link BuildingDao} using Hibernate.
 * 
 * @author TineL
 */
@Transactional
public class HibernateBuildingDao extends HibernateLoadAndSaveDao<Building>
  implements BuildingDao {}
