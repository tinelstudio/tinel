/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

/**
 * Interface to be implemented by beans that require special start-up method to
 * be invoked in order to start the application. In other words: for
 * applications.
 * 
 * @author TineL
 * @since 1.0
 */
public interface StartableBean {

  /**
   * Invoke to start the application.
   * 
   * @throws Exception In case of application start-up errors
   */
  void start() throws Exception;
}
