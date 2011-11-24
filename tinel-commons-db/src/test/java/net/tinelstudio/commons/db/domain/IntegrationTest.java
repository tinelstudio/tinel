
package net.tinelstudio.commons.db.domain;

import static org.junit.Assert.assertNotNull;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TineL
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:db-context.template.xml")
public class IntegrationTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  public void testIntegrity() {
    assertNotNull(sessionFactory);
  }
}
