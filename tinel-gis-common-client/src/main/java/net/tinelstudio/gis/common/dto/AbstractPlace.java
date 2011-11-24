/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

/**
 * The abstract implementation of the {@link Place}.
 * 
 * @author TineL
 */
public abstract class AbstractPlace implements Place {

  private String continentName;
  private String countryName;
  private String regionName;
  private String townName;
  private String note;

  public String getContinentName() {
    return this.continentName;
  }

  public void setContinentName(String continentName) {
    this.continentName=continentName;
  }

  public String getCountryName() {
    return this.countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName=countryName;
  }

  public String getRegionName() {
    return this.regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName=regionName;
  }

  public String getTownName() {
    return this.townName;
  }

  public void setTownName(String townName) {
    this.townName=townName;
  }

  public String getNote() {
    return this.note;
  }

  public void setNote(String note) {
    this.note=note;
  }
}