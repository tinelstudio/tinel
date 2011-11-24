/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.spring;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.springframework.web.HttpRequestHandler;

/**
 * @author TineL
 */
public class EmbeddedHttpRequestHandlerServletTest {

  protected EmbeddedHttpRequestHandlerServlet createInstance() {
    return new EmbeddedHttpRequestHandlerServlet();
  }

  @Test
  public void testSetTarget() {
    HttpRequestHandler target=createMock(HttpRequestHandler.class);
    EmbeddedHttpRequestHandlerServlet servlet=createInstance();
    servlet.setTarget(target);
    assertSame(target, servlet.getTarget());
  }
}
