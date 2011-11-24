/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.domain;

import java.util.Properties;

/**
 * @author TineL
 */
public interface Map {

  String getName();

  boolean isEnabled();

  Properties getProperties();

  String getSource();

  String getComment();
}
