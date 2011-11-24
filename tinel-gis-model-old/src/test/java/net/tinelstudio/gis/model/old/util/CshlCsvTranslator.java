/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.model.old.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.tinelstudio.commons.spring.test.PerformanceTestExecutionListener;
import net.tinelstudio.gis.common.domain.Continent;
import net.tinelstudio.gis.model.dao.AddressDao;
import net.tinelstudio.gis.model.domain.Address;
import net.tinelstudio.gis.model.load.AddressMaker;
import net.tinelstudio.gis.model.old.SpringContexts;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

/**
 * New addresses: 486401. Run with JVM argument -Xmx768M min. Run time to local
 * DB: ~16 min.
 * 
 * @author TineL
 */
// Remove @Ignore if you want to manually test
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
  net.tinelstudio.gis.model.SpringContexts.DB_CONTEXT,
  net.tinelstudio.gis.model.load.SpringContexts.LOAD_CONTEXT,
  SpringContexts.OLD_DB_CONTEXT})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  PerformanceTestExecutionListener.class})
@Transactional
public class CshlCsvTranslator extends AddressMaker {

  private final String source="cshl.csv";
  // private final String source="test.csv";
  private final String csvFile="e:\\projects\\autrace\\csv\\cshl.csv";
  // private final String csvFile="test.csv";
  private final String csvFileEncoding="UTF-8";
  private final String defaultCountry="Slovenija";
  private final String defaultContinent=Continent.EUROPE;

  @Autowired
  private AddressDao addressDao;

  private List<Address> newAddresses=new ArrayList<Address>(1000000);

  // @Rollback
  @Rollback(false)
  @Test
  public void start() throws Exception {
    StopWatch sw=new StopWatch();
    sw.start();

    MessageFormat mf=new MessageFormat("{0},{1},{2},{3,number},{4,number}",
      Locale.US);

    logger.info("Reading CSV '"+csvFile+"'...");
    BufferedReader br=new BufferedReader(new InputStreamReader(
      new FileInputStream(csvFile), csvFileEncoding));

    String note="Source:"+source;

    logger.info("Parsing...");
    while (true) {
      String line=br.readLine();
      if (line==null) break;
      Object[] objs=mf.parse(line);

      String town=((String)objs[0]).trim();
      String street=((String)objs[1]).trim();
      String house=((String)objs[2]).trim();
      Double lat=(Double)objs[3];
      Double lng=(Double)objs[4];

      Address a=createAddress(lng, lat, defaultContinent, defaultCountry, null,
        town, street, house, note);
      newAddresses.add(a);
    }

    logger.info("Persisting new Addresses ["+newAddresses.size()+"]...");
    int i=1;
    for (Address a : newAddresses) {
      addressDao.save(a);
      if (i%1000==0) {
        logger.info("#"+i+": "+a);
      }
      i++;
    }

    sw.stop();
    logger.info("Finished in "+sw.getLastTaskTimeMillis()+" ms");
    logger.info("Committing transaction...");
  }
}