/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import net.tinelstudio.gis.model.SpringContexts;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The console program to test HQL queries.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=SpringContexts.DB_CONTEXT)
public class HqlTester {

  @Autowired
  private HibernateTemplate hibernateTemplate;

  @Test
  public void start() {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      try {
        System.out.println();
        System.out.println("HQL input:");

        String hql=br.readLine();
        List<?> list=hibernateTemplate.find(hql);

        System.out.println("HQL query result ["+list.size()+"]:");
        for (Object object : list) {
          System.out.println(object);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}