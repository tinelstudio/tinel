/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author TineL
 */
public class DistanceOnEarthTest {

  private final double DELTA=1e-6;

  @Test
  public void testNormalizeLongitude() {
    double l=0.0;
    double r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(0.0, r, DELTA);

    l=-143.17;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(-143.17, r, DELTA);

    l=112.372;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(112.372, r, DELTA);

    l=-180.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(-180.0, r, 0);

    l=180.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(180.0, r, 0);

    l=-181.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(179.0, r, 0);

    l=181.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(-179.0, r, 0);

    l=270.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(-90.0, r, 0);

    l=-270.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(90.0, r, 0);

    l=-360.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(0.0, r, 0);

    l=360.0;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(0.0, r, 0);

    l=1796.425;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(-3.575, r, DELTA);

    l=-1796.425;
    r=DistanceOnEarth.normalizeLongitude(l);
    Assert.assertEquals(3.575, r, DELTA);
  }

  @Test
  public void testDistance() {
    int distance=20015109;
    for (int i=-360; i<=360; i++) {
      double lng1=i;
      double lat1=0;
      double lng2=i+180;
      double lat2=0;

      int r=DistanceOnEarth.distance(lat1, lng1, lat2, lng2);
      Assert.assertEquals(distance, r);
    }

    double lng1=0;
    double lat1=-90;
    double lng2=0;
    double lat2=90;
    int r=DistanceOnEarth.distance(lat1, lng1, lat2, lng2);
    Assert.assertEquals(distance, r);

    DistanceOnEarth.distance(0, 0, 0, 0);

    DistanceOnEarth.distance(90, -560, -90, 312);

    try {
      DistanceOnEarth.distance(91, 0, 0, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.distance(-91, 0, 0, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.distance(0, 0, 91, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.distance(0, 0, -91, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }
  }

  @Test
  public void testDirection() {
    double r=DistanceOnEarth.direction(0, 0, 90, 0);
    Assert.assertEquals(180.0, r, DELTA);

    r=DistanceOnEarth.direction(90, 0, 0, 0);
    Assert.assertEquals(0.0, r, DELTA);

    r=DistanceOnEarth.direction(0, 0, 0, 90);
    Assert.assertEquals(270.0, r, DELTA);

    r=DistanceOnEarth.direction(0, 90, 0, 0);
    Assert.assertEquals(90.0, r, DELTA);

    r=DistanceOnEarth.direction(45, 90, 0, 0);
    Assert.assertEquals(45.0, r, DELTA);

    try {
      DistanceOnEarth.direction(91, 0, 0, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.direction(-91, 0, 0, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.direction(0, 0, 91, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }

    try {
      DistanceOnEarth.direction(0, 0, -91, 0);
      Assert.fail("No IllegalArgumentException");

    } catch (IllegalArgumentException e) {
      // OK
    }
  }
}
