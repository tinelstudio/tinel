/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common;

/**
 * Indicates that the service has experienced an exception.
 * 
 * @author TineL
 */
public class ServiceException extends Exception {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-4081495452676252610L;

  /**
   * Constructs a new exception with the given cause. The cause is converted to
   * message.
   * 
   * @param cause the cause
   */
  public ServiceException(Throwable cause) {
    super(cause.toString()+": "+cause.getMessage());
  }
}
