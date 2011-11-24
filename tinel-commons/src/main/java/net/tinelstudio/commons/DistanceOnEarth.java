/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.commons;

/**
 * Provides distance and direction methods for the spherical Earth.<br>
 * <br>
 * In memory of Andrej.
 * 
 * @author andrej
 * @author TineL
 * @since 1.0
 */
public final class DistanceOnEarth {

  /**
   * Earth's authalic sphere radius in meters defined by:<br>
   * Ellipsoid [GRS 1980 Authalic Sphere], EPSG 7048
   * 
   * @see "http://www.epsg-registry.org"
   */
  public static final int R=6371007;

  /**
   * Normalizes the given longitude to be within interval [-180,180] degrees.
   * 
   * @param longitude the longitude to normalize
   * @return a normalized longitude (in [-180,180])
   */
  public static double normalizeLongitude(double longitude) {
    while (longitude<-180) {
      longitude+=360;
    }
    while (longitude>180) {
      longitude-=360;
    }
    return longitude;
  }

  /**
   * Calculates a great circle (or spherical) distance between two points on the
   * globe. Parameters represent the corresponding latitudes (within interval
   * [-90,90] degrees) and longitudes (which are normalized to interval
   * [-180,180] degrees) of these two points. The method returns appropriate
   * result in meters.
   * 
   * @param latitude1 the latitude of the 1st point (must be in [-90,90])
   * @param longitude1 the longitude of the 1st point
   * @param latitude2 the latitude of the 2nd point (must be in [-90,90])
   * @param longitude2 the longitude of the 2nd point
   * @return distance in meters
   * @throws IllegalArgumentException If one or both latitudes are outside
   *         interval [-90,90] degrees
   */
  public static int distance(double latitude1, double longitude1,
    double latitude2, double longitude2) throws IllegalArgumentException {
    if (Math.abs(latitude1)>90||Math.abs(latitude2)>90) {
      throw new IllegalArgumentException("The latitude(s) out of bounds");
    }
    longitude1=normalizeLongitude(longitude1);
    longitude2=normalizeLongitude(longitude2);
    double lat1Rad=Math.toRadians(latitude1);
    double long1Rad=Math.toRadians(longitude1);
    double lat2Rad=Math.toRadians(latitude2);
    double long2Rad=Math.toRadians(longitude2);
    double cosOfAngle=Math.cos(lat1Rad)*Math.cos(lat2Rad)
      *Math.cos(long1Rad-long2Rad)+Math.sin(lat1Rad)*Math.sin(lat2Rad);
    return (int)Math.rint(R*Math.acos(cosOfAngle));
  }

  /**
   * Calculates a "longitudinal" distance between two points on the globe with
   * the same latitude. Parameters represent the corresponding longitudes (which
   * are normalized to interval [-180,180] degrees) and the joint latitude
   * (within interval [-90,90]). The method returns appropriate result in
   * meters. The result is positive if longitude1 > longitude2 and negative if
   * longitude1 < longitude2.
   * 
   * @param longitude1 the longitude of the 1st point
   * @param longitude2 the longitude of the 2nd point
   * @param latitude the latitude of both points (must be in [-90,90])
   * @return distance in meters (positive or negative)
   * @throws IllegalArgumentException If latitude is outside interval [-90,90]
   *         degrees
   */
  public static int distanceLongitude(double longitude1, double longitude2,
    double latitude) {
    if (Math.abs(latitude)>90) {
      throw new IllegalArgumentException("The latitude out of bounds");
    }
    longitude1=normalizeLongitude(longitude1);
    longitude2=normalizeLongitude(longitude2);
    double latRad=Math.toRadians(latitude);
    double angle=longitude1-longitude2;
    double absAngle=Math.abs(angle);
    if (absAngle>180) {
      angle=(absAngle-360)*(int)(angle/absAngle);
    }
    return (int)Math.rint(R*Math.cos(latRad)*Math.toRadians(angle));
  }

  /**
   * Calculates a "latitudinal" distance between two points on the globe
   * (representing the distance between two points with the same longitude or
   * lying on the same meridian). Parameters represent the corresponding
   * latitudes (within interval [-90,90] degrees). The method returns
   * appropriate result in meters. The result is positive if latitude1 <
   * latitude2 and negative if latitude1 > latitude2.
   * 
   * @param latitude1 the 1st latitude on the same meridian (must be in
   *        [-90,90])
   * @param latitude2 the 2nd latitude on the same meridian (must be in
   *        [-90,90])
   * @return distance in meters (positive or negative)
   * @throws IllegalArgumentException If one or both latitudes are outside
   *         interval [-90,90] degrees
   */
  public static int distanceLatitude(double latitude1, double latitude2) {
    if (Math.abs(latitude1)>90||Math.abs(latitude2)>90) {
      throw new IllegalArgumentException("The latitude(s) out of bounds");
    }
    return (int)Math.rint(R*Math.toRadians(latitude2-latitude1));
  }

  /**
   * Calculates a longitude which is at the given "longitudinal" distance from
   * the given longitude on the same given latitude. Parameters represent the
   * corresponding longitude (which is normalized to interval [-180,180]
   * degrees), distance (positive or negative) in meters and latitude (within
   * interval [-90,90]). If the given distance is positive, the calculated new
   * longitude will be east of the given longitude and vice versa. On condition
   * that the given distance does not exceed half of the latitudinal
   * circumference, the method returns a new longitude (within interval
   * [-180,180] degrees).
   * 
   * @param longitude the longitude of the point
   * @param distance the distance (positive or negative) along the same parallel
   * @param latitude the latitude of the chosen parallel (must be in [-90,90])
   * @return a new latitude at the "longitudinal" distance away from the point
   * @throws IllegalArgumentException If latitude is outside interval [-90,90]
   *         degrees, and if distance exceeds half of the latitudinal
   *         circumference
   */
  public static double longitudeAtDistance(double longitude, int distance,
    double latitude) {
    if (Math.abs(latitude)>90) {
      throw new IllegalArgumentException("The latitude out of bounds");
    }
    double latRad=Math.toRadians(latitude);
    if (Math.abs(distance)>Math.PI*R*Math.cos(latRad)) {
      throw new IllegalArgumentException("The distance out of bounds");
    }
    longitude=normalizeLongitude(longitude);
    double newLong=longitude+180*(distance/(Math.PI*R*Math.cos(latRad)));
    double absNewLong=Math.abs(newLong);
    if (absNewLong>180) {
      newLong=(absNewLong-360)*(int)(newLong/absNewLong);
    }
    return newLong;
  }

  /**
   * Calculates a new latitude which is at the given "latitudinal" distance from
   * the given latitude. Parameters represent the corresponding latitude (within
   * interval [-90,90] degrees) and distance (positive or negative) in meters.
   * If the given distance is positive, the calculated new latitude is smaller
   * (more to the south) than the given latitude and vice versa. On condition
   * that the given distance does not exceed some limitations, the method
   * returns a new latitude (within interval [-90,90] degrees).
   * 
   * @param latitude the latitude of the point (must be in [-90,90])
   * @param distance the distance (positive or negative) along the same meridian
   * @return a latitude of the new point (the distance away from the point)
   * @throws IllegalArgumentException If latitude is outside interval [-90,90]
   *         degrees, and if distance exceeds some limitations
   */
  public static double latitudeAtDistance(double latitude, int distance) {
    if (Math.abs(latitude)>90) {
      throw new IllegalArgumentException("The latitude out of bounds");
    }
    double newLatitude=latitude-180*(distance/(Math.PI*R));
    if (Math.abs(newLatitude)>90) {
      throw new IllegalArgumentException("The distance out of bounds");
    }
    return newLatitude;
  }

  /**
   * Calculates the angle (on the globe) between direction from the 2nd point
   * towards the North Pole and direction from the 2nd point towards the 1st
   * point. The angle is measured clockwise and can occupy values between 0 and
   * 360 degrees (and where 360 degrees means undefinable - for antipodal
   * points).
   * 
   * @param latitude1 the latitude of the 1st point (must be in [-90,90])
   * @param longitude1 the longitude of the 1st point
   * @param latitude2 the latitude of the 2nd point (must be in [-90,90])
   * @param longitude2 the longitude of the 2nd point
   * @return an angle or direction of the 1st point with regard to 2nd one (in
   *         [0,360]). 360 degrees mean undefinable - for antipodal points.
   * @throws IllegalArgumentException If one or both latitudes are outside
   *         interval [-90,90] degrees
   */
  public static double direction(double latitude1, double longitude1,
    double latitude2, double longitude2) {
    if (Math.abs(latitude1)>90||Math.abs(latitude2)>90) {
      throw new IllegalArgumentException("The latitude(s) out of bounds");
    }
    longitude1=normalizeLongitude(longitude1);
    longitude2=normalizeLongitude(longitude2);
    if (longitude1==longitude2) {
      if (latitude1>=latitude2) {
        return 0;
      } else {
        return 180;
      }
    }
    if (Math.abs(longitude1-longitude2)==180) {
      if (latitude1+latitude2>0) {
        return 0;
      } else if (latitude1+latitude2<0) {
        return 180;
      } else {
        return 360;
      }
    }
    double lat1Rad=Math.toRadians(latitude1);
    double long1Rad=Math.toRadians(longitude1);
    double lat2Rad=Math.toRadians(latitude2);
    double long2Rad=Math.toRadians(longitude2);
    double a1=Math.cos(long1Rad)*Math.cos(lat1Rad);
    double a2=Math.sin(long1Rad)*Math.cos(lat1Rad);
    double a3=Math.sin(lat1Rad);
    double b1=Math.cos(long2Rad)*Math.cos(lat2Rad);
    double b2=Math.sin(long2Rad)*Math.cos(lat2Rad);
    double b3=Math.sin(lat2Rad);
    double c1=0;
    double c2=0;
    double c3=1;
    double d1=a2*b3-a3*b2;
    double d2=a3*b1-a1*b3;
    double d3=a1*b2-a2*b1;
    double e1=c2*b3-c3*b2;
    double e2=c3*b1-c1*b3;
    double e3=c1*b2-c2*b1;
    double angle=Math.toDegrees(Math.acos((d1*e1+d2*e2+d3*e3)
      /(Math.sqrt(d1*d1+d2*d2+d3*d3)*Math.sqrt(e1*e1+e2*e2+e3*e3))));
    if (longitude1>longitude2) {
      if (Math.abs(longitude1-longitude2)<180) {
        return angle;
      } else {
        return 360-angle;
      }
    } else {
      if (Math.abs(longitude2-longitude1)<180) {
        return 360-angle;
      } else {
        return angle;
      }
    }
  }

  /**
   * Calculates the distance from a given point on the globe to the area of
   * interest (AOI). More precisely, it finds the nearest point on the margin of
   * the AOI and returns the distance between the given point and that point. If
   * the given point is:<br>
   * <ul>
   * <li>outside the AOI, then the result is positive,</li>
   * <li>on the margin, then the result is 0,</li>
   * <li>inside the AOI, then the result is negative.</li>
   * </ul>
   * 
   * @param latitude the latitude of the given point (must be in [-90,90])
   * @param longitude the longitude of the given point
   * @param minLatitude The "lower fence" latitude of the AOI (must be in
   *        [-90,90])
   * @param minLongitude The "left fence" longitude of the AOI
   * @param maxLatitude The "upper fence" latitude of the AOI (must be in
   *        [-90,90])
   * @param maxLongitude The "right fence" longitude of the AOI
   * @return distance from the given point to the AOI in meters
   * @throws IllegalArgumentException If one or more latitudes are outside
   *         interval [-90,90] degrees, or if AOI is badly defined: minLatitude
   *         > maxLatitude
   */
  public static int distance(double latitude, double longitude,
    double minLatitude, double minLongitude, double maxLatitude,
    double maxLongitude) {
    if (Math.abs(latitude)>90||Math.abs(minLatitude)>90
      ||Math.abs(maxLatitude)>90||Math.abs(longitude)>180) {
      throw new IllegalArgumentException("The latitude(s) out of bounds");
    }
    if (minLatitude>maxLatitude) {
      throw new IllegalArgumentException(
        "The area definition error: minLatitude > maxLatitude");
    }
    minLongitude=normalizeLongitude(minLongitude);
    maxLongitude=normalizeLongitude(maxLongitude);
    if (minLongitude<=maxLongitude) {
      if (latitude<=minLatitude) {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          return distance(latitude, longitude, minLatitude, longitude);
        } else {
          return Math.min(distance(latitude, longitude, minLatitude,
            minLongitude), distance(latitude, longitude, minLatitude,
            maxLongitude));
        }
      } else if (latitude>=maxLatitude) {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          return distance(latitude, longitude, maxLatitude, longitude);
        } else {
          return Math.min(distance(latitude, longitude, maxLatitude,
            minLongitude), distance(latitude, longitude, maxLatitude,
            maxLongitude));
        }
      } else {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          return -Math.min(Math.min(distance(latitude, longitude, minLatitude,
            longitude), distance(latitude, longitude, maxLatitude, longitude)),
            Math.min(distance(latitude, longitude, latitude, minLongitude),
              distance(latitude, longitude, latitude, maxLongitude)));
        } else {
          return Math.min(
            distance(latitude, longitude, latitude, minLongitude), distance(
              latitude, longitude, latitude, maxLongitude));
        }
      }
    } else {
      if (latitude<=minLatitude) {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          return distance(latitude, longitude, minLatitude, longitude);
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return distance(latitude, longitude, minLatitude, minLongitude);
          } else {
            return distance(latitude, longitude, minLatitude, maxLongitude);
          }
        }
      } else if (latitude>=maxLatitude) {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          return distance(latitude, longitude, maxLatitude, longitude);
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return distance(latitude, longitude, maxLatitude, minLongitude);
          } else {
            return distance(latitude, longitude, maxLatitude, maxLongitude);
          }
        }
      } else {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          return -Math.min(Math.min(distance(latitude, longitude, minLatitude,
            longitude), distance(latitude, longitude, maxLatitude, longitude)),
            Math.min(distance(latitude, longitude, latitude, minLongitude),
              distance(latitude, longitude, latitude, maxLongitude)));
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return distance(latitude, longitude, latitude, minLongitude);
          } else {
            return distance(latitude, longitude, latitude, maxLongitude);
          }
        }
      }
    }
  }

  /**
   * Calculates the direction from a given point on the globe towards the area
   * of interest (AOI). More precisely, it finds the nearest point on the margin
   * of the AOI and calculates the direction from the given point towards that
   * point. For more see {@link #direction(double, double, double, double)}.) *
   * 
   * @param latitude the latitude of the given point (must be in [-90,90])
   * @param longitude the longitude of the given point
   * @param minLatitude The "lower fence" latitude of the AOI (must be in
   *        [-90,90])
   * @param minLongitude The "left fence" longitude of the AOI
   * @param maxLatitude The "upper fence" latitude of the AOI (must be in
   *        [-90,90])
   * @param maxLongitude The "right fence" longitude of the AOI
   * @return direction from the given point towards the AOI in degrees (in
   *         [0,360])
   * @throws IllegalArgumentException If one or more latitudes are outside
   *         interval [-90,90] degrees, or if AOI is badly defined: minLatitude
   *         > maxLatitude
   * @see #direction(double, double, double, double)
   */
  public static double direction(double latitude, double longitude,
    double minLatitude, double minLongitude, double maxLatitude,
    double maxLongitude) {
    if (Math.abs(latitude)>90||Math.abs(minLatitude)>90
      ||Math.abs(maxLatitude)>90||Math.abs(longitude)>180) {
      throw new IllegalArgumentException("The latitude(s) out of bounds");
    }
    if (minLatitude>maxLatitude) {
      throw new IllegalArgumentException(
        "The area definition error: minLatitude > maxLatitude");
    }
    if (minLongitude<=maxLongitude) {
      if (latitude<=minLatitude) {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          return direction(minLatitude, longitude, latitude, longitude);
        } else {
          if (distance(latitude, longitude, minLatitude, minLongitude)<=distance(
            latitude, longitude, minLatitude, maxLongitude)) {
            return direction(minLatitude, minLongitude, latitude, longitude);
          } else {
            return direction(minLatitude, maxLongitude, latitude, longitude);
          }
        }
      } else if (latitude>=maxLatitude) {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          return direction(maxLatitude, longitude, latitude, longitude);
        } else {
          if (distance(latitude, longitude, maxLatitude, minLongitude)<=distance(
            latitude, longitude, maxLatitude, maxLongitude)) {
            return direction(maxLatitude, minLongitude, latitude, longitude);
          } else {
            return direction(maxLatitude, maxLongitude, latitude, longitude);
          }
        }
      } else {
        if ((longitude>=minLongitude)&&(longitude<=maxLongitude)) {
          if (Math.min(distance(latitude, longitude, maxLatitude, longitude),
            distance(latitude, longitude, minLatitude, longitude))<=Math.min(
            distance(latitude, longitude, latitude, maxLongitude), distance(
              latitude, longitude, latitude, minLongitude))) {
            if (distance(latitude, longitude, maxLatitude, longitude)<=distance(
              latitude, longitude, minLatitude, longitude)) {
              return direction(maxLatitude, longitude, latitude, longitude);
            } else {
              return direction(minLatitude, longitude, latitude, longitude);
            }
          } else {
            if (distance(latitude, longitude, latitude, maxLongitude)<=distance(
              latitude, longitude, latitude, minLongitude)) {
              return direction(latitude, maxLongitude, latitude, longitude);
            } else {
              return direction(latitude, minLongitude, latitude, longitude);
            }
          }
        } else {
          if (distance(latitude, longitude, latitude, minLongitude)<=distance(
            latitude, longitude, latitude, maxLongitude)) {
            return direction(latitude, minLongitude, latitude, longitude);
          } else {
            return direction(latitude, maxLongitude, latitude, longitude);
          }
        }
      }
    } else {
      if (latitude<=minLatitude) {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          return direction(minLatitude, longitude, latitude, longitude);
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return direction(minLatitude, minLongitude, latitude, longitude);
          } else {
            return direction(minLatitude, maxLongitude, latitude, longitude);
          }
        }
      } else if (latitude>=maxLatitude) {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          return direction(maxLatitude, longitude, latitude, longitude);
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return direction(maxLatitude, minLongitude, latitude, longitude);
          } else {
            return direction(maxLatitude, maxLongitude, latitude, longitude);
          }
        }
      } else {
        if ((longitude>=minLongitude)||(longitude<=maxLongitude)) {
          if (Math.min(distance(latitude, longitude, maxLatitude, longitude),
            distance(latitude, longitude, minLatitude, longitude))<=Math.min(
            distance(latitude, longitude, latitude, maxLongitude), distance(
              latitude, longitude, latitude, minLongitude))) {
            if (distance(latitude, longitude, maxLatitude, longitude)<=distance(
              latitude, longitude, minLatitude, longitude)) {
              return direction(maxLatitude, longitude, latitude, longitude);
            } else {
              return direction(minLatitude, longitude, latitude, longitude);
            }
          } else {
            if (distance(latitude, longitude, latitude, maxLongitude)<=distance(
              latitude, longitude, latitude, minLongitude)) {
              return direction(latitude, maxLongitude, latitude, longitude);
            } else {
              return direction(latitude, minLongitude, latitude, longitude);
            }
          }
        } else {
          if ((minLongitude-longitude)<=(longitude-maxLongitude)) {
            return direction(latitude, minLongitude, latitude, longitude);
          } else {
            return direction(latitude, maxLongitude, latitude, longitude);
          }
        }
      }
    }
  }
}
