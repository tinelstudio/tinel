/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author TineL
 */
public class BaseEntityTest {

  protected BaseEntity createInstance() {
    return new BaseEntity();
  }

  @Test
  public void testSetId() {
    Long id=(long)(Math.random()*1000);
    BaseEntity entity=createInstance();
    entity.setId(id);
    assertSame(id, entity.getId());
  }
}
