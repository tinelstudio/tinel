/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.spring;

import java.io.FileNotFoundException;

import net.tinelstudio.commons.LoggingThreadUncaughtExceptionHandler;

/**
 * Convenience class that features simple method for custom log4j configuration
 * using Spring's {@link org.springframework.util.Log4jConfigurer}.
 * 
 * @author TineL
 * @see org.springframework.util.Log4jConfigurer
 */
public class Log4jConfigurer {

  /**
   * Initializes log4j from the standard location on the file system (
   * {@code file:log4j.properties}) with the refresh interval of 10 seconds. If
   * that file cannot be found, it tries standard location in the classpath (
   * {@code classpath:log4j.properties}). If log4j initialization is successful,
   * it also sets the default uncaught exception handler
   * {@link LoggingThreadUncaughtExceptionHandler} to this thread.
   * 
   * @throws FileNotFoundException If neither the file system and the classpath
   *         properties file cannot be found
   */
  public static void initStandardLogging() throws FileNotFoundException {
    try {
      org.springframework.util.Log4jConfigurer.initLogging(
        "file:log4j.properties", 10000L);

    } catch (FileNotFoundException e) {
      // File not found. Try classpath
      org.springframework.util.Log4jConfigurer
        .initLogging("classpath:log4j.properties");
    }
    // Log uncaught exceptions like OutOfMemoryError
    Thread
      .setDefaultUncaughtExceptionHandler(new LoggingThreadUncaughtExceptionHandler());
  }
}
