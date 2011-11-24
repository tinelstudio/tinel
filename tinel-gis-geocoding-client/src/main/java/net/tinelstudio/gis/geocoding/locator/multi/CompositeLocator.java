/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import net.tinelstudio.gis.geocoding.locator.Locator;

/**
 * This locator is a composite of multiple {@link Locator}s. During the search,
 * all locators here specified are used, one by one.
 * 
 * @author TineL
 * @see MultiLocator
 */
public class CompositeLocator extends AbstractMultiLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-2111086471387471671L;

  public CompositeLocator() {}

  /**
   * Creates composite locator with the specified locators.
   * 
   * @param locators the {@link Locator}s (<code>null</code> not permitted)
   */
  public CompositeLocator(Locator... locators) {
    setLocators(locators);
  }
}
