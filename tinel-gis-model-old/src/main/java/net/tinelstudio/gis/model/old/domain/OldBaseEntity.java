/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

/**
 * The old base entity.
 * 
 * @author TineL
 * @param <E> the domain object
 */
@MappedSuperclass
public class OldBaseEntity<E> {

  @Id
  private Long gid;

  @Column
  private String label;

  @Column
  @Type(type="org.hibernatespatial.GeometryUserType")
  private E geom;

  // ##################################################

  public Long getGid() {
    return this.gid;
  }

  public void setGid(Long gid) {
    this.gid=gid;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label=label;
  }

  public E getGeom() {
    return this.geom;
  }

  public void setGeom(E geom) {
    this.geom=geom;
  }
}
