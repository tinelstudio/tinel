/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Logs any thread uncaught exception. If the {@link OutOfMemoryError} occurs,
 * it then exits the JVM with an exit status 1, because the application is in
 * unusable, unknown, hanged state.
 * 
 * @author TineL
 * @since 1.0
 */
public class LoggingThreadUncaughtExceptionHandler implements
  Thread.UncaughtExceptionHandler {

  protected final Log logger=LogFactory.getLog(getClass());

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    // Just log
    logger.error("Thread exception occurred in thread '"+t.getName()+"'", e);
    e.printStackTrace();

    // Exit on OutOfMemoryError
    if (e instanceof OutOfMemoryError) {
      logger.fatal("OutOfMemoryError occurred. Exiting");
      System.exit(1);
    }
  }
}
