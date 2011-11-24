/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator.multi;

import net.tinelstudio.gis.reversegeocoding.locator.Locator;

/**
 * This locator is a special composite of multiple {@link Locator}s. During the
 * search, the results of only one locator of all here specified are returned -
 * which first finds something.<br>
 * If the first locator does not find anything, the search is fall-backed to
 * next locator and so on.
 * 
 * @author TineL
 * @see MultiLocator
 */
public class FallbackLocator extends AbstractMultiLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-8590974252787379052L;

  public FallbackLocator() {}

  /**
   * Creates fall-back locator with the specified locators.
   * 
   * @param locators the {@link Locator}s (<code>null</code> not permitted)
   */
  public FallbackLocator(Locator... locators) {
    setLocators(locators);
  }
}
