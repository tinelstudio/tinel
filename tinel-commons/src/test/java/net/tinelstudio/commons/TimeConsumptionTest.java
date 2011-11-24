/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author TineL
 */
public class TimeConsumptionTest {

  @Test
  public void testReset() throws Exception {
    TimeConsumption.startTime();
    Thread.sleep(100);
    TimeConsumption.endTime();
    TimeConsumption.reset();

    assertEquals(0, TimeConsumption.getTotalTime());
    assertEquals(Double.NaN, TimeConsumption.getAverageTime(), 0.0);
  }

  @Test
  public void testTotalTime() throws Exception {
    long st1=System.currentTimeMillis();
    TimeConsumption.startTime();
    Thread.sleep(1000);
    long et1=System.currentTimeMillis();
    TimeConsumption.endTime();
    long st2=System.currentTimeMillis();
    TimeConsumption.startTime();
    Thread.sleep(1000);
    long et2=System.currentTimeMillis();
    TimeConsumption.endTime();
    long st3=System.currentTimeMillis();
    TimeConsumption.startTime();
    Thread.sleep(1000);
    long et3=System.currentTimeMillis();
    TimeConsumption.endTime();

    long totalTime=et3-st3+et2-st2+et1-st1;
    double aveTime=totalTime/3.0;

    assertEquals(totalTime, TimeConsumption.getTotalTime(), 300);
    assertEquals(aveTime, TimeConsumption.getAverageTime(), 100);
  }
}
