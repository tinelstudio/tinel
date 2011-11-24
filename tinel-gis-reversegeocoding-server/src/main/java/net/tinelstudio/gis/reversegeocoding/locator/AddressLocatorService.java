/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.reversegeocoding.locator;

import java.util.ArrayList;
import java.util.List;

import net.tinelstudio.gis.common.dto.AddressConverter;
import net.tinelstudio.gis.common.dto.AddressDto;
import net.tinelstudio.gis.common.dto.Place;
import net.tinelstudio.gis.model.domain.Address;

import com.vividsolutions.jts.geom.Point;

/**
 * The {@link AddressLocator} service.
 * 
 * @author TineL
 * @see AddressLocator
 */
public class AddressLocatorService extends AbstractLocatorService {

  private AddressConverter addressConverter;

  @Override
  public List<? extends Place> findNearest() {
    // Phase I: Prepare
    Point point=convertToPoint(getSearchLocation());

    // Phase II: Search
    List<Address> adresses=getFindingDao().findNearestAddresses(point,
      getMaxDistanceMeters(), getMaxResults());

    // Phase III: Convert
    List<AddressDto> dtos=new ArrayList<AddressDto>(adresses.size());
    for (Address a : adresses) {
      AddressDto dto=getAddressConverter().convertAddress(a);
      dtos.add(dto);
    }
    return dtos;
  }

  public AddressConverter getAddressConverter() {
    if (this.addressConverter==null) {
      this.addressConverter=new AddressConverter();
    }
    return this.addressConverter;
  }

  public void setAddressConverter(AddressConverter addressConverter) {
    this.addressConverter=addressConverter;
  }
}
