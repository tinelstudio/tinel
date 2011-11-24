/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.domain;

import static org.easymock.classextension.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * @author TineL
 */
public class AddressRangeTest extends BaseEntityTest {

  @Override
  protected AddressRange createInstance() {
    return new AddressRange();
  }

  @Test
  public void testSetFromAddress() {
    Address address=createMock(Address.class);
    AddressRange ar=createInstance();
    ar.setFromAddress(address);
    assertSame(address, ar.getFromAddress());
  }

  @Test
  public void testSetToAddress() {
    Address address=createMock(Address.class);
    AddressRange ar=createInstance();
    ar.setToAddress(address);
    assertSame(address, ar.getToAddress());
  }
}
