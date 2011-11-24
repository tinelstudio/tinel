/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class AbstractEnablableServiceTest {

  protected AbstractEnablableService createInstance() {
    return new AbstractEnablableService() {};
  }

  @Test
  public void testSetEnabled() {
    AbstractEnablableService abstractEnablableService=createInstance();
    abstractEnablableService.setEnabled(true);
    assertTrue(abstractEnablableService.isEnabled());
  }

  @Test
  public void testSetDisabled() {
    AbstractEnablableService abstractEnablableService=createInstance();
    abstractEnablableService.setEnabled(false);
    assertFalse(abstractEnablableService.isEnabled());
  }
}
