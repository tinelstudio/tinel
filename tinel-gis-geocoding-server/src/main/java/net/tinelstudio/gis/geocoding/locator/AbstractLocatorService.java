/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import net.tinelstudio.gis.model.dao.FindingDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * The abstract implementation of {@link LocatorService}.
 * 
 * @author TineL
 */
public abstract class AbstractLocatorService extends AbstractLocator implements
  LocatorService {

  protected final Log logger=LogFactory.getLog(getClass());

  private FindingDao findingDao;

  /**
   * Splits the search string by white spaces & punctuations.
   * 
   * @return the split search string (<code>null</code> not possible)
   * @throws IllegalArgumentException If the search string is <code>null</code>
   */
  protected String[] parseSearchString() {
    Assert.hasText(getSearchString(), "Search string cannot be null or empty");

    // One or more white spaces or punctuations (see java.util.regex.Pattern)
    final String regex="[\\s\\p{Punct}]+";
    String[] keys=getSearchString().split(regex);
    return keys;
  }

  @Override
  public FindingDao getFindingDao() {
    return this.findingDao;
  }

  @Override
  public void setFindingDao(FindingDao findingDao) {
    this.findingDao=findingDao;
  }
}
