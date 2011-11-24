/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.common.dto;

import java.io.Serializable;

/**
 * Defines a place without actual spatial data, only world-wide geographic
 * names.
 * 
 * @author TineL
 */
public interface Place extends Serializable {

  String getContinentName();

  String getCountryName();

  String getRegionName();

  String getTownName();

  String getNote();
}
