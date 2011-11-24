/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.multi;

import java.util.Arrays;

import net.tinelstudio.gis.geocoding.locator.AbstractLocator;
import net.tinelstudio.gis.geocoding.locator.Locator;

import org.springframework.util.Assert;

/**
 * The abstract implementation of {@link MultiLocator}.
 * 
 * @author TineL
 */
public abstract class AbstractMultiLocator extends AbstractLocator implements
  MultiLocator {

  private Locator[] locators;

  @Override
  public Locator[] getLocators() {
    return this.locators;
  }

  @Override
  public void setLocators(Locator... locators) {
    Assert.notNull(locators, "Locators cannot be null");
    this.locators=locators;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append(" [getSearchString()=");
    builder.append(this.getSearchString());
    builder.append(", getMaxResults()=");
    builder.append(this.getMaxResults());
    builder.append(", locators=");
    builder.append(Arrays.toString(this.locators));
    builder.append("]");
    return builder.toString();
  }
}
