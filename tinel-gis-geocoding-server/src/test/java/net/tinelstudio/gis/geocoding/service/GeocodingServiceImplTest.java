/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.service;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.List;

import net.tinelstudio.gis.common.MaxResultsLimitExceededException;
import net.tinelstudio.gis.common.NotEnabledException;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.common.service.AbstractEnablableServiceTest;
import net.tinelstudio.gis.geocoding.locator.Locator;
import net.tinelstudio.gis.geocoding.locator.LocatorService;
import net.tinelstudio.gis.geocoding.locator.LocatorServiceFactory;
import net.tinelstudio.gis.model.dao.FindingDao;

import org.junit.Test;

/**
 * @author TineL
 */
public class GeocodingServiceImplTest extends AbstractEnablableServiceTest {

  @Override
  protected GeocodingServiceImpl createInstance() {
    GeocodingServiceImpl service=new GeocodingServiceImpl();
    service.setEnabled(true);
    return service;
  }

  @Test
  public void testSetFindingDao() {
    FindingDao findingDao=createMock(FindingDao.class);
    GeocodingServiceImpl service=createInstance();
    service.setFindingDao(findingDao);
    assertSame(findingDao, service.getFindingDao());
  }

  @Test
  public void testSetLocatorServiceFactory() {
    LocatorServiceFactory factory=createMock(LocatorServiceFactory.class);
    GeocodingServiceImpl service=createInstance();
    service.setLocatorServiceFactory(factory);
    assertSame(factory, service.getLocatorServiceFactory());
  }

  @Test
  public void testSetMaxResultsLimit() throws Exception {
    int maxResultsLimit=(int)(Math.random()*10000-100);
    GeocodingServiceImpl service=createInstance();
    service.setMaxResultsLimit(maxResultsLimit);
    assertEquals(maxResultsLimit, service.getMaxResultsLimit());
  }

  @Test(expected=NotEnabledException.class)
  public void testDisabledGetMaxResultsLimit() throws Exception {
    GeocodingServiceImpl service=createInstance();
    service.setEnabled(false);
    service.getMaxResultsLimit();
  }

  @Test(expected=MaxResultsLimitExceededException.class)
  public void testFindLimitedMaxResults() throws Exception {
    // Init
    int maxResultsLimit=(int)(Math.random()*10000+1); // Limited
    int maxResults=-(int)(Math.random()*100); // Unlimited
    Locator locator=createMock(Locator.class);
    expect(locator.getMaxResults()).andReturn(maxResults);
    replay(locator);

    // Run
    GeocodingServiceImpl service=createInstance();
    service.setMaxResultsLimit(maxResultsLimit);
    service.find(locator);
  }

  @Test(expected=MaxResultsLimitExceededException.class)
  public void testFindTooManyMaxResults() throws Exception {
    // Init
    int maxResultsLimit=(int)(Math.random()*10000+1); // Limited
    int maxResults=maxResultsLimit+(int)(Math.random()*100+1); // Limited+
    Locator locator=createMock(Locator.class);
    expect(locator.getMaxResults()).andReturn(maxResults);
    replay(locator);

    // Run
    GeocodingServiceImpl service=createInstance();
    service.setMaxResultsLimit(maxResultsLimit);
    service.find(locator);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindUnlimitedMaxResults() throws Exception {
    // Init
    int maxResultsLimit=-(int)(Math.random()*100); // Unlimited
    Locator locator=createMock(Locator.class);
    LocatorService locatorService=createMock(LocatorService.class);
    expect(locatorService.find()).andReturn(Collections.EMPTY_LIST);
    LocatorServiceFactory locatorServiceFactory=createMock(LocatorServiceFactory.class);
    expect(locatorServiceFactory.createLocatorService(locator, null))
      .andReturn(locatorService);
    replay(locator, locatorService, locatorServiceFactory);

    // Run
    GeocodingServiceImpl service=createInstance();
    service.setMaxResultsLimit(maxResultsLimit);
    service.setLocatorServiceFactory(locatorServiceFactory);
    service.find(locator);

    // Verify
    verify(locator, locatorService, locatorServiceFactory);
  }

  @Test(expected=NotEnabledException.class)
  public void testDisabledFind() throws Exception {
    GeocodingServiceImpl service=createInstance();
    service.setEnabled(false);
    service.find(null);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFind() throws Exception {
    // Init
    Place place=createMock(Place.class);
    List<Place> places=Collections.singletonList(place);
    Locator locator=createMock(Locator.class);
    expect(locator.getMaxResults()).andReturn(1).anyTimes();
    LocatorService locatorService=createMock(LocatorService.class);
    expect(locatorService.find()).andReturn((List)places);
    FindingDao findingDao=createMock(FindingDao.class);
    LocatorServiceFactory locatorServiceFactory=createMock(LocatorServiceFactory.class);
    expect(locatorServiceFactory.createLocatorService(locator, findingDao))
      .andReturn(locatorService);
    replay(locator, locatorService, locatorServiceFactory);

    // Run
    GeocodingServiceImpl service=createInstance();
    service.setFindingDao(findingDao);
    service.setLocatorServiceFactory(locatorServiceFactory);
    List<? extends Place> res=service.find(locator);

    // Verify
    verify(locator, locatorService, locatorServiceFactory);
    assertEquals(places, res);
  }

  @Test
  public void testGetServerTime() {
    long time=System.currentTimeMillis();
    GeocodingServiceImpl service=createInstance();
    // Give it max 100 ms time difference (due to multi-threaded system)
    assertEquals(time, service.getServerTime(), 100);
  }
}