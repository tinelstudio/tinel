/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import java.util.HashMap;
import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.domain.Street;

/**
 * The cost function that computes cost by the length of the street multiplied
 * with the weight of the street.
 * 
 * @author TineL
 * @see LevelLengthCost
 */
public class LevelLengthCostFunction extends LevelLengthCost implements
  CostFunction {

  /**
   * Constructs a cost function without level/weight mapping defined.
   */
  public LevelLengthCostFunction() {}

  /**
   * Constructs a cost function without specified level/weight mapping.
   * 
   * @param levelWeightMap the level/weight mapping where all existing levels
   *        must be mapped (<code>null</code> not permitted)
   * @throws IllegalArgumentException If any level is not mapped
   * @see #setLevelWeightMap(Map)
   */
  public LevelLengthCostFunction(Map<Level, Double> levelWeightMap)
    throws IllegalArgumentException {
    super(levelWeightMap);
  }

  @Override
  public double computeCost(Street street) {
    return street.getLengthMeters()*getLevelWeightMap().get(street.getLevel());
  }

  @Override
  public void setLevelWeightMap(Map<Level, Double> levelValueMap)
    throws IllegalArgumentException {
    /*
     * Copy map so entries can't be changed in the future and that the best map
     * implementation is used for sure.
     */
    super.setLevelWeightMap(new HashMap<Level, Double>(levelValueMap));
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("LevelLengthCostFunction [getLevelWeightMap()=");
    builder.append(this.getLevelWeightMap());
    builder.append("]");
    return builder.toString();
  }
}
