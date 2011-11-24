/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.astar;

import java.util.Collection;

import net.tinelstudio.gis.model.dao.FindingDao;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;

/**
 * The default A* routing search algorithm.<br>
 * <br>
 * <b>Warning:</b> Create this object anew for every run. Do not reuse it.<br>
 * <br>
 * 
 * @author TineL
 * @see AbstractAStarSearchAlgorithm
 */
public class DefaultAStarSearchAlgorithm extends AbstractAStarSearchAlgorithm {

  private FindingDao findingDao;

  @Override
  protected Collection<Street> findConnectedStreets(StreetNode streetNode) {
    return getFindingDao().findConnectedStreets(streetNode);
  }

  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }
}
