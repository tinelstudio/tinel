/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author TineL
 */
public class AbstractPlaceTest {

  protected AbstractPlace createInstance() {
    return new AbstractPlace() {};
  }

  @Test
  public void testSetTownName() {
    String name=Double.toHexString(Math.random());
    AbstractPlace agl=createInstance();
    agl.setTownName(name);
    assertSame(name, agl.getTownName());
  }

  @Test
  public void testSetRegionName() {
    String name=Double.toHexString(Math.random());
    AbstractPlace agl=createInstance();
    agl.setRegionName(name);
    assertSame(name, agl.getRegionName());
  }

  @Test
  public void testSetCountryName() {
    String name=Double.toHexString(Math.random());
    AbstractPlace agl=createInstance();
    agl.setCountryName(name);
    assertSame(name, agl.getCountryName());
  }

  @Test
  public void testSetContinentName() {
    String name=Double.toHexString(Math.random());
    AbstractPlace agl=createInstance();
    agl.setContinentName(name);
    assertSame(name, agl.getContinentName());
  }

  @Test
  public void testSetNote() {
    String note=Double.toHexString(Math.random());
    AbstractPlace agl=createInstance();
    agl.setNote(note);
    assertSame(note, agl.getNote());
  }
}
