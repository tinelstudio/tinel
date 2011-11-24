/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator.multi;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;
import net.tinelstudio.gis.reversegeocoding.locator.Locator;

import org.junit.Test;

/**
 * @author TineL
 */
public class CompositeLocatorTest extends AbstractMultiLocatorTest {

  @Override
  protected CompositeLocator createInstance() {
    return new CompositeLocator();
  }

  protected CompositeLocator createInstance(Locator... locators) {
    return new CompositeLocator(locators);
  }

  @Test
  public void testConstructor() {
    Locator l1=createMock(Locator.class);
    Locator l2=createMock(Locator.class);
    Locator l3=createMock(Locator.class);
    CompositeLocator locator=createInstance(l1, l2, l3);
    Locator[] locators=locator.getLocators();
    assertSame(l1, locators[0]);
    assertSame(l2, locators[1]);
    assertSame(l3, locators[2]);
  }
}
