/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator.multi;

import net.tinelstudio.gis.reversegeocoding.locator.AddressLocator;
import net.tinelstudio.gis.reversegeocoding.locator.BuildingLocator;
import net.tinelstudio.gis.reversegeocoding.locator.Locator;
import net.tinelstudio.gis.reversegeocoding.locator.StreetLocator;

/**
 * The full composite locator with all already specified {@link Locator}s.
 * 
 * @author TineL
 * @see CompositeLocator
 */
public class FullCompositeLocator extends CompositeLocator {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-820885816273995554L;

  public FullCompositeLocator() {
    setLocators(new AddressLocator(), new StreetLocator(),
      new BuildingLocator());
  }
}
