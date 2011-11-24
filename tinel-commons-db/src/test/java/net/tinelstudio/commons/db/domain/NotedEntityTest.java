/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.db.domain;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author TineL
 */
public class NotedEntityTest extends BaseEntityTest {

  @Override
  protected NotedEntity createInstance() {
    return new NotedEntity() {};
  }

  @Test
  public void testSetNote() {
    String note="Note";
    NotedEntity entity=createInstance();
    entity.setNote(note);
    assertSame(note, entity.getNote());
  }
}
