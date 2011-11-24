/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm.cost;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;

import org.junit.Test;

/**
 * @author TineL
 */
public class CostFunctionFactoryTest {

  @Test(expected=IllegalArgumentException.class)
  public void testCreateCostFunction() {
    Cost cost=createMock(Cost.class);
    CostFunctionFactory costFunctionFactory=new CostFunctionFactory();
    costFunctionFactory.createCostFunction(cost);
  }

  @Test
  public void testCreateLengthCostFunction() {
    LengthCost lengthCost=createMock(LengthCost.class);
    CostFunctionFactory costFunctionFactory=new CostFunctionFactory();
    CostFunction costFunction=costFunctionFactory
      .createCostFunction(lengthCost);
    assertTrue(costFunction instanceof LengthCostFunction);
  }

  @Test
  public void testCreateLevelLengthCostFunction() {
    // Init
    Map<Level, Double> levelWeightMap=new HashMap<Level, Double>();
    levelWeightMap.put(Level.MAJOR, -1.0);
    levelWeightMap.put(Level.MEDIUM, -1.0);
    levelWeightMap.put(Level.MINOR, -1.0);
    levelWeightMap.put(Level.UNKNOWN, -1.0);

    LevelLengthCost levelLengthCost=createMock(LevelLengthCost.class);
    expect(levelLengthCost.getLevelWeightMap()).andReturn(levelWeightMap);

    replay(levelLengthCost);

    // Run
    CostFunctionFactory costFunctionFactory=new CostFunctionFactory();
    CostFunction costFunction=costFunctionFactory
      .createCostFunction(levelLengthCost);

    // Verify
    verify(levelLengthCost);
    assertTrue(costFunction instanceof LevelLengthCostFunction);
    assertEquals(levelWeightMap, ((LevelLengthCostFunction)costFunction)
      .getLevelWeightMap());
  }
}
