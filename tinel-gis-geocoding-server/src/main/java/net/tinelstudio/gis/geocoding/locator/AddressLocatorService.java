/*
 * Licensed under a Creative Commons Attribution 2.5 Slovenia License
 * http://creativecommons.org/licenses/by/2.5/si/
 * 2009 TineL Studio
 */

package net.tinelstudio.gis.geocoding.locator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tinelstudio.gis.common.dto.AddressConverter;
import net.tinelstudio.gis.common.dto.AddressDto;
import net.tinelstudio.gis.model.domain.Address;

/**
 * The {@link AddressLocator} service.
 * 
 * @author TineL
 * @see AddressLocator
 */
public class AddressLocatorService extends AbstractLocatorService {

  private AddressConverter addressConverter;

  @Override
  public List<AddressDto> find() {
    // Phase I: Parse
    String[] keys=parseSearchString();

    // Phase II: Elements assigned to keys
    List<String> names=new ArrayList<String>();
    List<String> houseNumbers=new ArrayList<String>();

    // Separate ordinary words from house numbers
    // Starting with digit, following any number of any characters
    final String houseNumberRegex="[0-9].*";
    for (String key : keys) {
      if (key.matches(houseNumberRegex)) {
        houseNumbers.add(key);

      } else {
        names.add(key);
      }
    }

    if (houseNumbers.isEmpty()) {
      if (logger.isInfoEnabled()) {
        logger.info("No house number(s) found in search string, "
          +"leaving locator with no search");
      }

      // No house number(s) found in search string, leaving without results
      return Collections.emptyList();
    }

    // Phase III: Search
    List<Address> addresses=getFindingDao().findAddresses(names, houseNumbers,
      getMaxResults());

    // Phase IV: Convert
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
