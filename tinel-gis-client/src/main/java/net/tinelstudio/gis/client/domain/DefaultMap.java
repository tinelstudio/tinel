/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.domain;

import java.util.Properties;

/**
 * @author TineL
 */
public class DefaultMap implements Map {

  private String name;
  private boolean enabled;
  private String source;
  private Properties properties;
  private String comment;

  @Override
  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name=name;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled=enabled;
  }

  @Override
  public String getSource() {
    return this.source;
  }

  public void setSource(String source) {
    this.source=source;
  }

  @Override
  public Properties getProperties() {
    return this.properties;
  }

  public void setProperties(Properties properties) {
    this.properties=properties;
  }

  @Override
  public String getComment() {
    return this.comment;
  }

  public void setComment(String comment) {
    this.comment=comment;
  }
}
