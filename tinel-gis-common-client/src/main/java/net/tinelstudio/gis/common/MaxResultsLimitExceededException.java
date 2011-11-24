/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common;

/**
 * Indicates that the maximum results limit has been exceeded.
 * 
 * @author TineL
 */
public class MaxResultsLimitExceededException extends Exception {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-2385074273017995542L;

  /**
   * Constructs a new exception with the specified detail message.
   * 
   * @param message the detail message
   */
  public MaxResultsLimitExceededException(String message) {
    super(message);
  }
}
