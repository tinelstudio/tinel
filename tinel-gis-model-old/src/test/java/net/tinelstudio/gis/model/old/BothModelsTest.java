/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
  net.tinelstudio.gis.model.SpringContexts.DB_CONTEXT,
  SpringContexts.OLD_DB_CONTEXT})
public class BothModelsTest {

  @Resource
  // By field name
  private Object oldSessionFactory;

  @Resource
  // By field name
  private Object sessionFactory;

  @Test
  public void testIntergrity() {
    assertNotNull(oldSessionFactory);
    assertNotNull(sessionFactory);
  }
}