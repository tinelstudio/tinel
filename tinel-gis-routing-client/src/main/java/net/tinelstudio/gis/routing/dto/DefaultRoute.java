/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.routing.dto;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.StreetDto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.operation.linemerge.LineMerger;

/**
 * The default implementation of {@link Route}.
 * 
 * @author TineL
 */
public class DefaultRoute implements Route {

  /** The <code>serialVersionUID</code>. */
  private static final long serialVersionUID=-7839781844467651524L;

  // Make it non-serializable (static)
  private static final Log logger=LogFactory.getLog(DefaultRoute.class);

  private List<StreetDto> streets;

  private Coordinate startCoordinate;

  private double cost;

  private long timeTaken;

  @Override
  public List<StreetDto> getStreets() {
    return this.streets;
  }

  public void setStreets(List<StreetDto> streets) {
    Assert.notNull(streets, "Streets cannot be null");
    this.streets=streets;
  }

  @Override
  public double getCost() {
    return this.cost;
  }

  public void setCost(double cost) {
    this.cost=cost;
  }

  @Override
  public long getTimeTaken() {
    return this.timeTaken;
  }

  public void setTimeTaken(long timeTaken) {
    this.timeTaken=timeTaken;
  }

  public Coordinate getStartCoordinate() {
    return this.startCoordinate;
  }

  public void setStartCoordinate(Coordinate startCoordinate) {
    this.startCoordinate=startCoordinate;
  }

  @Override
  public LineString mergeStreets() {
    if (getStreets().isEmpty()) return null;
    if (getStartCoordinate()==null) {
      throw new NullPointerException("startCoordinate==null");
    }

    // Merge
    LineMerger lm=new LineMerger();
    for (StreetDto s : getStreets()) {
      lm.add(s.getLineString());
    }

    // Check for GIS data errors
    @SuppressWarnings("unchecked")
    List<LineString> lines=(List<LineString>)lm.getMergedLineStrings();
    if (lines.size()>1&&logger.isErrorEnabled()) {
      // Prepare GIS data error analysis
      List<Coordinate> problematicCoors=new ArrayList<Coordinate>(
        lines.size()*2);
      for (LineString ls : lines) {
        problematicCoors.add(ls.getCoordinateN(0));
        problematicCoors.add(ls.getCoordinateN(ls.getCoordinateSequence()
          .size()-1));
      }
      logger.error("Streets merging is incomplete. "
        +"There is at least one GIS data error that prevents streets from "
        +"being merged into one string of lines. "
        +"Problematic streets form one or more nodes of degree 3 or more. "
        +"Look around these coordinates to find error(s): "+problematicCoors
        +". Only first merged string of lines will be returned");
    }

    // Check direction
    LineString line=lines.get(0);
    Coordinate firstC=line.getCoordinateN(0);
    if (!firstC.equals(getStartCoordinate())) {
      // Line is reversed
      line=line.reverse();
    }
    return line;
  }

  @Override
  public String toString() {
    StringBuilder builder=new StringBuilder();
    builder.append("DefaultRoute [startCoordinate=");
    builder.append(this.startCoordinate);
    builder.append(", cost=");
    builder.append(this.cost);
    builder.append(", timeTaken=");
    builder.append(this.timeTaken);
    builder.append(", mergeStreets()=");
    builder.append(this.mergeStreets());
    builder.append("]");
    return builder.toString();
  }
}
