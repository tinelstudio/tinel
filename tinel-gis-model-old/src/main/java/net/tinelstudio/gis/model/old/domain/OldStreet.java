/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.vividsolutions.jts.geom.LineString;

/**
 * The super class of old street entities.
 * 
 * @author TineL
 */
@MappedSuperclass
public class OldStreet extends OldBaseEntity<LineString> {

  @Column
  private Boolean one_way;

  // ##################################################

  public Boolean getOne_way() {
    return this.one_way;
  }

  public void setOne_way(Boolean oneWay) {
    this.one_way=oneWay;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("OldStreet [getGid()=");
    builder.append(this.getGid());
    builder.append(", getLabel()=");
    builder.append(this.getLabel());
    builder.append(", one_way=");
    builder.append(this.one_way);
    builder.append("]");
    return builder.toString();
  }
}