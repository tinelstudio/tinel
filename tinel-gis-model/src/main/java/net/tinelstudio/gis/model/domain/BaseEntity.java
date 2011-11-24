/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * The base entity providing only identifier (ID).
 * 
 * @author TineL
 */
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  // ##################################################

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id=id;
  }
}
