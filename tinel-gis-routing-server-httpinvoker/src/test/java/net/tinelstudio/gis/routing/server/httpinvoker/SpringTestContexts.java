/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.server.httpinvoker;

/**
 * @author TineL
 */
public interface SpringTestContexts {

  /** Client default Spring context. */
  String CLIENT_CONTEXT="classpath:client-context.xml";

  /** Jetty server default Spring context. */
  String JETTY_CONTEXT="classpath:jetty-context.xml";
}
