/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * The base entity with note.
 * 
 * @author TineL
 */
@MappedSuperclass
public class NotedEntity extends BaseEntity {

  @Column
  private String note;

  // ##################################################

  public String getNote() {
    return this.note;
  }

  public void setNote(String note) {
    this.note=note;
  }
}
