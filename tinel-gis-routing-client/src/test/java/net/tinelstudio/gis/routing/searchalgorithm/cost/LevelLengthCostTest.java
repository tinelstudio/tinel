/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;

import org.junit.Test;

/**
 * @author TineL
 */
public class LevelLengthCostTest {

  protected Map<Level, Double> getLevelWeightMap() {
    Map<Level, Double> levelWeightMap=new HashMap<Level, Double>();
    levelWeightMap.put(Level.MAJOR, -1.0);
    levelWeightMap.put(Level.MEDIUM, -1.0);
    levelWeightMap.put(Level.MINOR, -1.0);
    levelWeightMap.put(Level.UNKNOWN, -1.0);
    return levelWeightMap;
  }

  protected LevelLengthCost createInstance() {
    return new LevelLengthCost();
  }

  protected LevelLengthCost createInstance(Map<Level, Double> levelWeightMap) {
    return new LevelLengthCost(levelWeightMap);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testSetIncompleteLevelWeightMap() {
    LevelLengthCost cost=createInstance();
    Map<Level, Double> levelWeightMap=getLevelWeightMap();
    levelWeightMap.remove(Level.UNKNOWN); // Map without one level
    cost.setLevelWeightMap(levelWeightMap);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIncompleteConstructor() {
    Map<Level, Double> levelWeightMap=getLevelWeightMap();
    levelWeightMap.remove(Level.MINOR); // Map without one level
    createInstance(levelWeightMap);
  }

  @Test
  public void testSetLevelWeightMap() {
    LevelLengthCost cost=createInstance();
    Map<Level, Double> levelWeightMap=getLevelWeightMap();
    cost.setLevelWeightMap(levelWeightMap);
    assertEquals(levelWeightMap, cost.getLevelWeightMap());
  }

  @Test
  public void testConstructor() {
    Map<Level, Double> levelWeightMap=getLevelWeightMap();
    LevelLengthCost cost=createInstance(levelWeightMap);
    assertEquals(levelWeightMap, cost.getLevelWeightMap());
  }
}
