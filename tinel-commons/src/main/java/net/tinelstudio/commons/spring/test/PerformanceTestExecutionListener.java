/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons.spring.test;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * <code>TestExecutionListener</code> which measures the time consumption of the
 * test methods. After the method test, it logs the consumed time.<br>
 * The time measuring is useful especially if the method testing is repetitive:
 * if the method is annotated with {@link Repeat}. It logs the total consumed
 * time as well as the per-repeat consumed time.
 * 
 * @author TineL
 * @since 1.0
 */
public class PerformanceTestExecutionListener extends
  AbstractTestExecutionListener {

  private final Log logger=LogFactory.getLog(getClass());

  private int count=0;
  private final StopWatch stopWatch=new StopWatch("testWatch");

  /**
   * Starts the time measuring if the test method is going to be called for the
   * first time (is annotated with {@link Repeat}) or for the only time (without
   * {@link Repeat}).
   */
  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    Method testMethod=testContext.getTestMethod();
    Assert.notNull(testMethod,
      "The test method of the supplied TestContext must not be null");

    if (this.count==0) {
      // Testing the method for the first or only time
      this.stopWatch.start(testMethod.getName());
    }
  }

  /**
   * Logs the total and per-repeat consumed time if the test method was tested
   * for the last time (is annotated with {@link Repeat}) or for the only time
   * (without {@link Repeat}).
   */
  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    Method testMethod=testContext.getTestMethod();
    Assert.notNull(testMethod,
      "The test method of the supplied TestContext must not be null");

    this.count++;

    // Check if repetition of the method test is over
    Repeat repeat=testMethod.getAnnotation(Repeat.class);
    boolean repetitionOver=(repeat==null||repeat.value()==this.count);

    if (repetitionOver) {
      /*
       * This method will not be tested again: stop watch and clean counter to
       * be prepared for the next method test
       */
      this.stopWatch.stop();
      if (logger.isInfoEnabled()) {
        long timeElapsedTotal=this.stopWatch.getLastTaskTimeMillis();
        double timeElapsedPerTest=timeElapsedTotal*1.0/count;
        logger.info(timeElapsedTotal+" ms total ("+count+" repeats), "
          +timeElapsedPerTest+" ms per repeat of test "+testMethod.getName());
      }
      this.count=0;
    }
  }
}