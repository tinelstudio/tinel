/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;

import org.springframework.util.Assert;

/**
 * The cost function that is defined with the length of the street, that is
 * between node n-1 and node n, and applied weight of the street. The weight of
 * the street conforms to the level of the street; the level/weight mapping is
 * specified by {@link #setLevelWeightMap(Map)}.
 * 
 * @author TineL
 * @see Cost
 */
public class LevelLengthCost implements Cost {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-1003453488486647214L;

  private Map<Level, Double> levelWeightMap;

  /**
   * Constructs a cost without level/weight mapping defined.
   */
  public LevelLengthCost() {}

  /**
   * Constructs a cost with specified level/weight mapping.
   * 
   * @param levelWeightMap the level/weight mapping where all existing levels
   *        must be mapped (<code>null</code> not permitted)
   * @throws IllegalArgumentException If any level is not mapped
   * @see #setLevelWeightMap(Map)
   */
  public LevelLengthCost(Map<Level, Double> levelWeightMap)
    throws IllegalArgumentException {
    super();
    setLevelWeightMap(levelWeightMap);
  }

  /**
   * @return the level/weight mapping
   */
  public Map<Level, Double> getLevelWeightMap() {
    return this.levelWeightMap;
  }

  /**
   * @param levelWeightMap the level/weight mapping where all existing levels
   *        must be mapped (<code>null</code> not permitted)
   * @throws IllegalArgumentException If any level is not mapped
   */
  public void setLevelWeightMap(Map<Level, Double> levelWeightMap)
    throws IllegalArgumentException {
    // Check if all levels are mapped
    for (Level level : Level.values()) {
      Assert.notNull(levelWeightMap.get(level), "Level "+level.toString()
        +" not mapped");
    }
    this.levelWeightMap=levelWeightMap;
  }

  @Override
  public String toString() {
    return "LevelLengthCost [levelWeightMap="+this.levelWeightMap+"]";
  }
}
