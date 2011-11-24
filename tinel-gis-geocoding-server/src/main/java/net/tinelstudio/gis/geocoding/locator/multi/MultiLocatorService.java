/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import java.util.List;

import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.LocatorService;

/**
 * Defines the service (server side) of the {@link MultiLocator}.
 * 
 * @author TineL
 * @see MultiLocator
 * @see LocatorService
 */
public interface MultiLocatorService extends LocatorService {

  /**
   * @return the multiple {@link Locator}s
   * @see #setLocators(List)
   */
  List<LocatorService> getLocators();

  /**
   * Sets the multiple {@link LocatorService}s to be used.
   * 
   * @param locators the locators (<code>null</code> not permitted)
   */
  void setLocators(List<LocatorService> locators);
}
