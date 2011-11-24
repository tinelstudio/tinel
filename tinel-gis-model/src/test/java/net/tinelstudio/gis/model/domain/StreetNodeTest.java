/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.vividsolutions.jts.geom.Point;

/**
 * @author TineL
 */
public class StreetNodeTest extends BaseEntityTest {

  @Override
  protected StreetNode createInstance() {
    return new StreetNode();
  }

  @Test
  public void testSetPoint() {
    Point point=createMock(Point.class);
    StreetNode streetNode=createInstance();
    streetNode.setPoint(point);
    assertSame(point, streetNode.getPoint());
  }
}
