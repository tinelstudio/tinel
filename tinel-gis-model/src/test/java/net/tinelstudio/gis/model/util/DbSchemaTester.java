/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.util;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import net.tinelstudio.gis.model.SpringContexts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.hibernate.tool.hbm2ddl.DatabaseMetadata;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
public class DbSchemaTester {

  private final Log logger=LogFactory.getLog(getClass());

  @Autowired
  private LocalSessionFactoryBean sessionFactory;

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Test
  public void updateDb() throws Exception {
    logger.info("Generating database schema for Hibernate SessionFactory");
    String[] sql=(String[])hibernateTemplate.execute(new HibernateCallback() {
      @Override
      public Object doInHibernate(Session session) throws HibernateException,
        SQLException {
        Configuration config=sessionFactory.getConfiguration();
        // Copied from LocalSessionFactoryBean.updateDatabaseSchema()
        @SuppressWarnings("deprecation")
        Connection con=session.connection();
        Dialect dialect=Dialect.getDialect(config.getProperties());
        DatabaseMetadata metadata=new DatabaseMetadata(con, dialect);
        String[] sql=config.generateSchemaUpdateScript(dialect, metadata);
        return sql;
      }
    });

    logger.info("Schema generated:");
    for (String line : sql) {
      logger.info(line);
    }

    // Warn if schema is different
    if (sql.length!=0) {
      fail("DB schema differences found!");
    }
  }

  // @Test
  public void dropDb() throws Exception {
    sessionFactory.dropDatabaseSchema();
  }
}