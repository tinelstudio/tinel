/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.spring;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

/**
 * The servlet inspired by {@link HttpRequestHandlerServlet} that can be used
 * without a WEB application context with a specially embedded servlet container
 * like Jetty.
 * 
 * @author TineL
 * @since 1.0
 * @see HttpRequestHandlerServlet
 */
public class EmbeddedHttpRequestHandlerServlet extends HttpServlet {

  private HttpRequestHandler target;

  @Override
  protected void service(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
    try {
      getTarget().handleRequest(request, response);

    } catch (HttpRequestMethodNotSupportedException ex) {
      String[] supportedMethods=ex.getSupportedMethods();
      if (supportedMethods!=null) {
        response.setHeader("Allow", StringUtils.arrayToDelimitedString(
          supportedMethods, ", "));
      }
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, ex
        .getMessage());
    }
  }

  public HttpRequestHandler getTarget() {
    return this.target;
  }

  public void setTarget(HttpRequestHandler target) {
    this.target=target;
  }
}
