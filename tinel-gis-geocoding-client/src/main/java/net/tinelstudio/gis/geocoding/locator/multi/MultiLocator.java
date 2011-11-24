/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * Defines the multi locator. A multi locator is used to specify multiple
 * {@link Locator}s that are used when geographical places is to be found. A
 * multi locator is {@link Locator} itself.
 * 
 * @author TineL
 * @see Locator
 */
public interface MultiLocator extends Locator {

  /**
   * @return the multiple {@link Locator}s
   * @see #setLocators(Locator...)
   */
  Locator[] getLocators();

  /**
   * Sets the multiple {@link Locator}s to be used.
   * 
   * @param locators the locators (<code>null</code> not permitted)
   */
  void setLocators(Locator... locators);
}