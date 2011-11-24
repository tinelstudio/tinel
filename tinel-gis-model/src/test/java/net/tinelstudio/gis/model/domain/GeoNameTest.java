/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.junit.Assert.assertSame;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.junit.Test;

/**
 * @author TineL
 */
public class GeoNameTest extends BaseEntityTest {

  @Override
  protected GeoName createInstance() {
    return new GeoName();
  }

  @Test
  public void testSetName() {
    String name="GeoName";
    GeoName geoName=createInstance();
    geoName.setName(name);
    assertSame(name, geoName.getName());
  }

  @Test
  public void testSetType() {
    Type type=Type.BUILDING;
    GeoName geoName=createInstance();
    geoName.setType(type);
    assertSame(type, geoName.getType());
  }
}
