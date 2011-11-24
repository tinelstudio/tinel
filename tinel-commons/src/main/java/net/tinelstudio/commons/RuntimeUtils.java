/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

/**
 * Runtime utilities.
 * 
 * @author TineL
 * @since 1.2
 */
public class RuntimeUtils {

  /**
   * @return a maximum runtime memory in MB
   * @see Runtime#maxMemory()
   */
  public static int getMaxMemoryMB() {
    return (int)(Runtime.getRuntime().maxMemory()/1024/1024);
  }

  /**
   * @return a total runtime memory in MB
   * @see Runtime#totalMemory()
   */
  public static int getTotalMemoryMB() {
    return (int)(Runtime.getRuntime().totalMemory()/1024/1024);
  }

  /**
   * @return an approximate free runtime memory in MB
   * @see Runtime#freeMemory()
   */
  public static int getFreeMemoryMB() {
    return (int)(Runtime.getRuntime().freeMemory()/1024/1024);
  }

  /**
   * @return an approximate used runtime memory in MB
   */
  public static int getUsedMemoryMB() {
    return getTotalMemoryMB()-getFreeMemoryMB();
  }
}
