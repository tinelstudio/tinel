/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

/**
 * The abstract implementation of {@link Locator}.
 * 
 * @author TineL
 */
public abstract class AbstractLocator implements Locator {

  private String searchString;
  private int maxResults=-1;

  @Override
  public String getSearchString() {
    return this.searchString;
  }

  @Override
  public void setSearchString(String searchString) {
    this.searchString=searchString;
  }

  @Override
  public int getMaxResults() {
    return this.maxResults;
  }

  @Override
  public void setMaxResults(int maxResults) {
    this.maxResults=maxResults;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append(getClass().getSimpleName());
    builder.append(" [searchString=");
    builder.append(this.searchString);
    builder.append(", maxResults=");
    builder.append(this.maxResults);
    builder.append("]");
    return builder.toString();
  }
}
