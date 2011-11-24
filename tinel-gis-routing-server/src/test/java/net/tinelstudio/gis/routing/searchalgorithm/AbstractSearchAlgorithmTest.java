/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.searchalgorithm;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.tinelstudio.gis.common.dto.StreetConverter;
import net.tinelstudio.gis.common.dto.StreetDto;
import net.tinelstudio.gis.model.domain.Street;
import net.tinelstudio.gis.model.domain.StreetNode;
import net.tinelstudio.gis.routing.dto.Route;
import net.tinelstudio.gis.routing.searchalgorithm.cost.CostFunction;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author TineL
 */
public class AbstractSearchAlgorithmTest {

  protected AbstractSearchAlgorithm createInstance() {
    return new AbstractSearchAlgorithm() {
      @Override
      public Route run() throws TimeoutException {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test
  public void testSetStartNode() {
    StreetNode startNode=createMock(StreetNode.class);
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setStartNode(startNode);
    assertSame(startNode, searchAlg.getStartNode());
  }

  @Test
  public void testSetGoalNode() {
    StreetNode goalNode=createMock(StreetNode.class);
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setGoalNode(goalNode);
    assertSame(goalNode, searchAlg.getGoalNode());
  }

  @Test
  public void testSetCostFunction() {
    CostFunction costFunction=createMock(CostFunction.class);
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setCostFunction(costFunction);
    assertSame(costFunction, searchAlg.getCostFunction());
  }

  @Test
  public void testSetTimeout() {
    int timeout=(int)(Math.random()*200-100);
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setTimeout(timeout);
    assertEquals(timeout, searchAlg.getTimeout());
  }

  @Test
  public void testSetStreetConverter() {
    StreetConverter streetConverter=createMock(StreetConverter.class);
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setStreetConverter(streetConverter);
    assertSame(streetConverter, searchAlg.getStreetConverter());
  }

  @Test
  public void testComputeCost() {
    // Init
    Street street=createMock(Street.class);
    double g=Math.random()*100;
    CostFunction costFunction=createMock(CostFunction.class);
    expect(costFunction.computeCost(street)).andReturn(g);
    replay(costFunction);

    // Run
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setCostFunction(costFunction);
    double r=searchAlg.computeCost(street);

    // Verify
    verify(costFunction);
    assertEquals(g, r, 0.0001);
  }

  @Test
  public void testCreateRoute() {
    // Init
    Coordinate startCoordinate=createMock(Coordinate.class);
    Street street1=createMock(Street.class);
    Street street2=createMock(Street.class);
    Street street3=createMock(Street.class);
    List<Street> streets=Arrays.asList(street1, street2, street3);
    double cost=Math.random()*100;
    long timeTaken=System.currentTimeMillis();
    StreetDto streetDto1=createMock(StreetDto.class);
    StreetDto streetDto2=createMock(StreetDto.class);
    StreetDto streetDto3=createMock(StreetDto.class);
    StreetConverter streetConverter=createMock(StreetConverter.class);
    expect(streetConverter.convertStreet(street1)).andReturn(streetDto1);
    expect(streetConverter.convertStreet(street2)).andReturn(streetDto2);
    expect(streetConverter.convertStreet(street3)).andReturn(streetDto3);
    replay(streetConverter);

    // Run
    AbstractSearchAlgorithm searchAlg=createInstance();
    searchAlg.setStreetConverter(streetConverter);
    Route route=searchAlg
      .createRoute(streets, startCoordinate, cost, timeTaken);

    // Verify
    verify(streetConverter);
    List<StreetDto> streetDtos=route.getStreets();
    assertSame(streetDto1, streetDtos.get(0));
    assertSame(streetDto2, streetDtos.get(1));
    assertSame(streetDto3, streetDtos.get(2));
    assertEquals(cost, route.getCost(), 0.0001);
    assertEquals(timeTaken, route.getTimeTaken());
  }
}
