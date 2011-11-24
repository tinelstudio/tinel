/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.load;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tinelstudio.gis.model.dao.StreetNodeDao;
import net.tinelstudio.gis.model.domain.StreetNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
@Component
public class InMemoryStreetNodeMaker extends InMemoryGeoNameMaker {

  @Autowired
  private StreetNodeDao streetNodeDao;

  private HashMap<Coordinate, StreetNode> nodeMap;

  public StreetNode findOrCreateStreetNode(Point point) {
    if (this.nodeMap==null) {
      logger.info("Creating in-memory map of StreetNodes...");
      this.nodeMap=new HashMap<Coordinate, StreetNode>(10000);
      preloadStreetNodes(this.nodeMap);
      logger.info("Done");
    }

    StreetNode node=nodeMap.get(point.getCoordinate());
    if (node==null) {
      // Create and persist new street node
      node=new StreetNode();
      node.setPoint(point);

      // Persist is done with cascading

      nodeMap.put(point.getCoordinate(), node);
    }
    return node;
  }

  private void preloadStreetNodes(Map<Coordinate, StreetNode> streetNodes) {
    logger.info("Preloading all StreetNodes from DB...");
    List<StreetNode> nodes=streetNodeDao.loadAll();
    for (StreetNode sn : nodes) {
      streetNodes.put(sn.getPoint().getCoordinate(), sn);
    }
    logger.info("Done ["+streetNodes.size()+"]");
  }

  public int getStreetNodesCount() {
    return (nodeMap==null)?0:nodeMap.size();
  }

  public StreetNodeDao getStreetNodeDao() {
    return this.streetNodeDao;
  }

  public void setStreetNodeDao(StreetNodeDao streetNodeDao) {
    this.streetNodeDao=streetNodeDao;
  }
}
