/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator.bytype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.tinelstudio.gis.common.dto.AddressConverter;
import net.tinelstudio.gis.common.dto.AddressDto;
import net.tinelstudio.gis.model.domain.Address;

/**
 * The implementation of {@link ByTypeLocatorService} that searches for
 * Addresses.
 * 
 * @author TineL
 * @see ByTypeLocatorService
 */
public class DistinctAddressByTypeLocatorService extends
  AbstractByTypeLocatorService {

  private AddressConverter addressConverter;

  @Override
  public List<AddressDto> find() {
    // Phase I: Parsing
    String[] keys=parseSearchString();

    // Phase II: Elements assigned to keys
    List<String> names=Arrays.asList(keys);

    // Phase III: Searching
    List<Address> addresses=getFindingDao().findDistinctAddressesByType(names,
      getType(), getMaxResults());

    // Phase IV: Converting
    List<AddressDto> dtos=new ArrayList<AddressDto>(addresses.size());
    for (Address a : addresses) {
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
