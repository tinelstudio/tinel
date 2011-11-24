/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import net.tinelstudio.gis.model.SpringContexts;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The console program to test SQL queries.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
public class SqlTester {

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Test
  public void start() {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      try {
        System.out.println();
        System.out.println("SQL input:");

        final String sql=br.readLine();
        List<?> list=hibernateTemplate.executeFind(new HibernateCallback() {
          @Override
          public Object doInHibernate(Session session)
            throws HibernateException, SQLException {
            return session.createSQLQuery(sql).list();
          }
        });

        System.out.println("SQL query result ["+list.size()+"]:");
        for (Object object : list) {
          System.out.println(object);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}