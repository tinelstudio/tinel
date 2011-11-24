/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tinelstudio.gis.common.dto.DefaultStreetDto;
import net.tinelstudio.gis.common.dto.StreetDto;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author TineL
 */
public class DefaultRouteTest {

  protected DefaultRoute createInstance() {
    return new DefaultRoute();
  }

  @Test
  public void testSetStreets() {
    List<StreetDto> streets=Collections.emptyList();
    DefaultRoute route=createInstance();
    route.setStreets(streets);
    assertSame(streets, route.getStreets());
  }

  @Test
  public void testSetStartCoordinate() {
    Coordinate c=createMock(Coordinate.class);
    DefaultRoute route=createInstance();
    route.setStartCoordinate(c);
    assertSame(c, route.getStartCoordinate());
  }

  @Test
  public void testSetCost() {
    double cost=Math.random()*100;
    DefaultRoute route=createInstance();
    route.setCost(cost);
    assertEquals(cost, route.getCost(), 0.0001);
  }

  @Test
  public void testSetTimeTaken() {
    long cost=(long)(Math.random()*10000);
    DefaultRoute route=createInstance();
    route.setTimeTaken(cost);
    assertEquals(cost, route.getTimeTaken());
  }

  @Test
  public void testMergeEmptyStreets() {
    List<StreetDto> streets=Collections.emptyList();
    DefaultRoute route=createInstance();
    route.setStreets(streets);
    LineString lines=route.mergeStreets();
    assertNull(lines);
  }

  private static LineString line1;
  private static LineString line2;
  private static LineString line3;
  private static Coordinate startCoordinate;
  private static String mergedLineString;

  @BeforeClass
  public static void createLineStrings() throws Exception {
    startCoordinate=new Coordinate(14.514935, 46.050266);
    String lineString1="LINESTRING (14.514935 46.050266, 14.514571 46.049407)";
    String lineString2="LINESTRING (14.514571 46.049407, 14.514184 46.048507)";
    String lineString3="LINESTRING (14.514184 46.048507, 14.513777 46.047584)";
    mergedLineString="LINESTRING (14.514935 46.050266, 14.514571 46.049407, 14.514184 46.048507, 14.513777 46.047584)";

    GeometryFactory gf=new GeometryFactory(new PrecisionModel(), 4326);
    WKTReader wktReader=new WKTReader(gf);
    line1=(LineString)wktReader.read(lineString1);
    line2=(LineString)wktReader.read(lineString2);
    line3=(LineString)wktReader.read(lineString3);
  }

  @Test
  public void testMergeStreetsOrdered() {
    testMergeStreetsInternal(line1, line2, line3);
  }

  // Mixed

  @Test
  public void testMergeStreetsMixed132() {
    testMergeStreetsInternal(line1, line3, line2);
  }

  @Test
  public void testMergeStreetsMixed213() {
    testMergeStreetsInternal(line2, line1, line3);
  }

  @Test
  public void testMergeStreetsMixed231() {
    testMergeStreetsInternal(line2, line3, line1);
  }

  @Test
  public void testMergeStreetsMixed312() {
    testMergeStreetsInternal(line3, line1, line2);
  }

  @Test
  public void testMergeStreetsMixed321() {
    testMergeStreetsInternal(line3, line2, line1);
  }

  // Ordered reversed

  @Test
  public void testMergeStreetsOrderedReversed1() {
    testMergeStreetsInternal(line1.reverse(), line2, line3);
  }

  @Test
  public void testMergeStreetsOrderedReversed2() {
    testMergeStreetsInternal(line1, line2.reverse(), line3);
  }

  @Test
  public void testMergeStreetsOrderedReversed3() {
    testMergeStreetsInternal(line1, line2, line3.reverse());
  }

  @Test
  public void testMergeStreetsOrderedReversed12() {
    testMergeStreetsInternal(line1.reverse(), line2.reverse(), line3);
  }

  @Test
  public void testMergeStreetsOrderedReversed13() {
    testMergeStreetsInternal(line1.reverse(), line2, line3.reverse());
  }

  @Test
  public void testMergeStreetsOrderedReversed23() {
    testMergeStreetsInternal(line1, line2.reverse(), line3.reverse());
  }

  @Test
  public void testMergeStreetsOrderedReversed123() {
    testMergeStreetsInternal(line1.reverse(), line2.reverse(), line3.reverse());
  }

  // Mixed132 reversed

  @Test
  public void testMergeStreetsMixed132Reversed1() {
    testMergeStreetsInternal(line1.reverse(), line3, line2);
  }

  @Test
  public void testMergeStreetsMixed132Reversed2() {
    testMergeStreetsInternal(line1, line3.reverse(), line2);
  }

  @Test
  public void testMergeStreetsMixed132Reversed3() {
    testMergeStreetsInternal(line1, line3, line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed132Reversed12() {
    testMergeStreetsInternal(line1.reverse(), line3.reverse(), line2);
  }

  @Test
  public void testMergeStreetsMixed132Reversed13() {
    testMergeStreetsInternal(line1.reverse(), line3, line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed132Reversed23() {
    testMergeStreetsInternal(line1, line3.reverse(), line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed132Reversed123() {
    testMergeStreetsInternal(line1.reverse(), line3.reverse(), line2.reverse());
  }

  // Mixed213 reversed

  @Test
  public void testMergeStreetsMixed213Reversed1() {
    testMergeStreetsInternal(line2.reverse(), line1, line3);
  }

  @Test
  public void testMergeStreetsMixed213Reversed2() {
    testMergeStreetsInternal(line2, line1.reverse(), line3);
  }

  @Test
  public void testMergeStreetsMixed213Reversed3() {
    testMergeStreetsInternal(line2, line1, line3.reverse());
  }

  @Test
  public void testMergeStreetsMixed213Reversed12() {
    testMergeStreetsInternal(line2.reverse(), line1.reverse(), line3);
  }

  @Test
  public void testMergeStreetsMixed213Reversed13() {
    testMergeStreetsInternal(line2.reverse(), line1, line3.reverse());
  }

  @Test
  public void testMergeStreetsMixed213Reversed23() {
    testMergeStreetsInternal(line2, line1.reverse(), line3.reverse());
  }

  @Test
  public void testMergeStreetsMixed213Reversed123() {
    testMergeStreetsInternal(line2.reverse(), line1.reverse(), line3.reverse());
  }

  // Mixed231 reversed

  @Test
  public void testMergeStreetsMixed231Reversed1() {
    testMergeStreetsInternal(line2.reverse(), line3, line1);
  }

  @Test
  public void testMergeStreetsMixed231Reversed2() {
    testMergeStreetsInternal(line2, line3.reverse(), line1);
  }

  @Test
  public void testMergeStreetsMixed231Reversed3() {
    testMergeStreetsInternal(line2, line3, line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed231Reversed12() {
    testMergeStreetsInternal(line2.reverse(), line3.reverse(), line1);
  }

  @Test
  public void testMergeStreetsMixed231Reversed13() {
    testMergeStreetsInternal(line2.reverse(), line3, line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed231Reversed23() {
    testMergeStreetsInternal(line2, line3.reverse(), line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed231Reversed123() {
    testMergeStreetsInternal(line2.reverse(), line3.reverse(), line1.reverse());
  }

  // Mixed312 reversed

  @Test
  public void testMergeStreetsMixed312Reversed1() {
    testMergeStreetsInternal(line3.reverse(), line1, line2);
  }

  @Test
  public void testMergeStreetsMixed312Reversed2() {
    testMergeStreetsInternal(line3, line1.reverse(), line2);
  }

  @Test
  public void testMergeStreetsMixed312Reversed3() {
    testMergeStreetsInternal(line3, line1, line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed312Reversed12() {
    testMergeStreetsInternal(line3.reverse(), line1.reverse(), line2);
  }

  @Test
  public void testMergeStreetsMixed312Reversed13() {
    testMergeStreetsInternal(line3.reverse(), line1, line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed312Reversed23() {
    testMergeStreetsInternal(line3, line1.reverse(), line2.reverse());
  }

  @Test
  public void testMergeStreetsMixed312Reversed123() {
    testMergeStreetsInternal(line3.reverse(), line1.reverse(), line2.reverse());
  }

  // Mixed321 reversed

  @Test
  public void testMergeStreetsMixed321Reversed1() {
    testMergeStreetsInternal(line3.reverse(), line2, line1);
  }

  @Test
  public void testMergeStreetsMixed321Reversed2() {
    testMergeStreetsInternal(line3, line2.reverse(), line1);
  }

  @Test
  public void testMergeStreetsMixed321Reversed3() {
    testMergeStreetsInternal(line3, line2, line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed321Reversed12() {
    testMergeStreetsInternal(line3.reverse(), line2.reverse(), line1);
  }

  @Test
  public void testMergeStreetsMixed321Reversed13() {
    testMergeStreetsInternal(line3.reverse(), line2, line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed321Reversed23() {
    testMergeStreetsInternal(line3, line2.reverse(), line1.reverse());
  }

  @Test
  public void testMergeStreetsMixed321Reversed123() {
    testMergeStreetsInternal(line3.reverse(), line2.reverse(), line1.reverse());
  }

  private void testMergeStreetsInternal(LineString line1, LineString line2,
    LineString line3) {
    DefaultStreetDto street1=new DefaultStreetDto();
    street1.setLineString(line1);
    DefaultStreetDto street2=new DefaultStreetDto();
    street2.setLineString(line2);
    DefaultStreetDto street3=new DefaultStreetDto();
    street3.setLineString(line3);
    List<StreetDto> streets=new ArrayList<StreetDto>();
    Collections.addAll(streets, street1, street2, street3);

    DefaultRoute route=createInstance();
    route.setStreets(streets);
    route.setStartCoordinate(startCoordinate);
    LineString lines=route.mergeStreets();

    assertEquals(mergedLineString, lines.toString());
  }
}
