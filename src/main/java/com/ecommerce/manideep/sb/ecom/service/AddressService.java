package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, AppUser user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAllAddressesOfUser(AppUser user);

    AddressDTO updateUserAddressById(AppUser user ,Long addressId, AddressDTO addressDTO);
}
