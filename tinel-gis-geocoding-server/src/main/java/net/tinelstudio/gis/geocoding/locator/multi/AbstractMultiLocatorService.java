/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import java.util.List;

import net.tinelstudio.gis.geocoding.locator.AbstractLocatorService;
import net.tinelstudio.gis.geocoding.locator.LocatorService;

import org.springframework.util.Assert;

/**
 * The abstract implementation of {@link MultiLocatorService}.
 * 
 * @author TineL
 * @see MultiLocatorService
 * @see MultiLocator
 */
public abstract class AbstractMultiLocatorService extends
  AbstractLocatorService implements MultiLocatorService {

  private List<LocatorService> locators;

  @Override
  public List<LocatorService> getLocators() {
    return this.locators;
  }

  @Override
  public void setLocators(List<LocatorService> locators) {
    Assert.notNull(locators, "Locators cannot be null");
    this.locators=locators;
  }
}
