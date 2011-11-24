/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Point;

/**
 * The StreetNode entity.
 * 
 * @author TineL
 */
@Entity
@Cache(usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class StreetNode extends BaseEntity {

  @Column(nullable=false)
  @Type(type="org.hibernatespatial.GeometryUserType")
  private Point point;

  // @OneToMany(mappedBy="startNode", fetch=FetchType.EAGER)
  @OneToMany(mappedBy="startNode")
  // @Transient
  @Cache(usage=CacheConcurrencyStrategy.TRANSACTIONAL)
  private Set<Street> outStreets;

  // @OneToMany(mappedBy="endNode", fetch=FetchType.EAGER)
  @OneToMany(mappedBy="endNode")
  // @Transient
  @Cache(usage=CacheConcurrencyStrategy.TRANSACTIONAL)
  private Set<Street> inStreets;

  // ##################################################

  public Point getPoint() {
    return this.point;
  }

  public void setPoint(Point point) {
    this.point=point;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("StreetNode [id=").append(this.getId()).append("]");
    return builder.toString();
  }

  public Set<Street> getOutStreets() {
    return this.outStreets;
  }

  public void setOutStreets(Set<Street> outStreets) {
    this.outStreets=outStreets;
  }

  public Set<Street> getInStreets() {
    return this.inStreets;
  }

  public void setInStreets(Set<Street> inStreets) {
    this.inStreets=inStreets;
  }
}
