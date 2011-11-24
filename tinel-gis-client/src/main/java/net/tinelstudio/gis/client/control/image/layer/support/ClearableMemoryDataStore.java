/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.client.control.image.layer.support;

import java.util.Map;

import org.geotools.data.memory.MemoryDataStore;

/**
 * @author TineL
 */
public class ClearableMemoryDataStore extends MemoryDataStore {

  @SuppressWarnings("unchecked")
  public void removeAllFeatures(String key) {
    ((Map)this.memory.get(key)).clear();
  }
}
