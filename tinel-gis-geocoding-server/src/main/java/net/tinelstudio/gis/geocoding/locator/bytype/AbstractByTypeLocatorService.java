/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import net.tinelstudio.gis.geocoding.locator.AbstractLocatorService;
import net.tinelstudio.gis.model.domain.GeoName.Type;

import org.springframework.util.Assert;

/**
 * The abstract implementation of {@link ByTypeLocatorService}.
 * 
 * @author TineL
 */
public abstract class AbstractByTypeLocatorService extends
  AbstractLocatorService implements ByTypeLocatorService {

  private Type type;

  @Override
  public Type getType() {
    return this.type;
  }

  @Override
  public void setType(Type type) {
    Assert.notNull(type, "Type cannot be null");
    this.type=type;
  }
}
