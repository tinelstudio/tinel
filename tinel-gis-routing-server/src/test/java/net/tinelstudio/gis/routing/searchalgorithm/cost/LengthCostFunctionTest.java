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
import net.tinelstudio.gis.model.domain.Street;

import org.junit.Test;

/**
 * @author TineL
 */
public class LengthCostFunctionTest {

  @Test
  public void testComputeCost() {
    // Init
    int length=(int)(Math.random()*100000+1);
    Street street=createMock(Street.class);
    expect(street.getLengthMeters()).andReturn(length);
    replay(street);

    // Run
    LengthCostFunction cf=new LengthCostFunction();
    double cost=cf.computeCost(street);

    // Verify
    verify(street);
    assertEquals(length, cost, 0.0001);
  }
}
