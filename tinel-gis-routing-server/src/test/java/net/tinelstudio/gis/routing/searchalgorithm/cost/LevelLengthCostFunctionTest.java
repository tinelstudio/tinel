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

import java.util.Map;

import net.tinelstudio.gis.common.dto.StreetDto.Level;
import net.tinelstudio.gis.model.domain.Street;

import org.junit.Test;

/**
 * @author TineL
 */
public class LevelLengthCostFunctionTest extends LevelLengthCostTest {

  @Override
  protected LevelLengthCostFunction createInstance() {
    return new LevelLengthCostFunction();
  }

  @Override
  protected LevelLengthCostFunction createInstance(
    Map<Level, Double> levelWeightMap) {
    return new LevelLengthCostFunction(levelWeightMap);
  }

  @Test
  public void testComputeCost() {
    // Init
    int length=(int)(Math.random()*100000);
    Level level=Level.MAJOR;
    double weight=Math.random()+0.1;
    Street street=createMock(Street.class);
    expect(street.getLengthMeters()).andReturn(length);
    expect(street.getLevel()).andReturn(level);
    Map<Level, Double> levelWeightMap=getLevelWeightMap();
    levelWeightMap.put(Level.MAJOR, weight);
    replay(street);

    // Run
    LevelLengthCostFunction cf=createInstance();
    cf.setLevelWeightMap(levelWeightMap);
    double cost=cf.computeCost(street);

    // Verify
    verify(street);
    assertEquals(length*weight, cost, 0.0001);
  }
}
