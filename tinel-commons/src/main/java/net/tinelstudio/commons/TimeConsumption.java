/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

import java.io.PrintStream;

/**
 * This class provides the time consumption measure utility.<br>
 * It is meant for many repetitive measures of the same scope.<br>
 * Measures time between {@link #startTime()} and {@link #endTime()}. These pair
 * can be used many times - this utility counts starts/ends.
 * 
 * @author TineL
 * @since 1.0
 */
public class TimeConsumption {

  /** Consumed time. */
  private static long totalTime=0;

  /** Temporary start time. */
  private static long startTime=0;

  /** The number of measures. */
  private static long counter=0;

  /**
   * Starts measuring the time.<br>
   * Use it when some operation begins.
   */
  public static void startTime() {
    startTime=System.currentTimeMillis();
  }

  /**
   * Stops measuring the time.<br>
   * Use it when the same operation ends.
   */
  public static void endTime() {
    totalTime+=System.currentTimeMillis()-startTime;
    counter++;
  }

  /**
   * @return the average consumed time per measure
   */
  public static double getAverageTime() {
    // If counter is 0, it returns NaN, which is OK
    return (double)totalTime/counter;
  }

  /**
   * @return the total consumed time for all measures
   */
  public static long getTotalTime() {
    return totalTime;
  }

  /**
   * Prints the total consumed and average time and the number of measures.
   * 
   * @param stream the stream to print on (<code>null</code> not permitted)
   */
  public static void printTiming(PrintStream stream) {
    stream.println("Total time: "+getTotalTime()+"\tCounter: "+counter
      +"\tAverage time: "+getAverageTime());
  }

  /**
   * Resets the time consumption.
   */
  public static void reset() {
    totalTime=0;
    counter=0;
  }
}
