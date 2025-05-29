package com.ecommerce.manideep.sb.ecom.service;

import com.ecommerce.manideep.sb.ecom.exeptions.ResourseNotFoundExeption;
import com.ecommerce.manideep.sb.ecom.model.Address;
import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.payload.AddressDTO;
import com.ecommerce.manideep.sb.ecom.repositories.AddressRepo;
import com.ecommerce.manideep.sb.ecom.repositories.AppUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService{

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AddressRepo addressRepo ;

    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, AppUser user) {
        Address address = mapper.map(addressDTO,Address.class);
        address.setUser(user);
        List<Address> addresses = user.getAddresses();
        addresses.add(address);
        user.setAddresses(addresses);
        Address savedAddress = addressRepo.save(address);
        appUserRepo.save(user);
        return mapper.map(savedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {

        List<Address> addressList = addressRepo.findAll();
        List<AddressDTO> addressDTOS = addressList.stream().map(address ->
                mapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Address address = addressRepo.findById(addressId)
                .orElseThrow(()-> new ResourseNotFoundExeption(
                        "Address","address id",addressId));

        return mapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddressesOfUser(AppUser user) {
        List<Address> addresses = user.getAddresses();
        log.info("List of address : " + addresses);
        return addresses.stream().map(address ->
                        mapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateUserAddressById(Long addressId, AddressDTO addressDTO) {
       Address addressFromDB = addressRepo.findById(addressId).orElseThrow(() ->
               new ResourseNotFoundExeption("Address","Address id " ,addressId));

       log.info("Manideep printing user from  Address object : {} ",addressFromDB.getUser());
       log.info("Manideep printing country from  Address object : {} ",addressFromDB.getCountry());
       addressFromDB.setCity(addressDTO.getCity());
       addressFromDB.setPincode(addressDTO.getPincode());
       addressFromDB.setState(addressDTO.getState());
       addressFromDB.setCountry(addressDTO.getCountry());
       addressFromDB.setBuildingName(addressDTO.getBuildingName());
       addressFromDB.setStreet(addressDTO.getStreet());

       Address updatedAddress = addressRepo.save(addressFromDB);
       AppUser user = addressFromDB.getUser();
       List<Address> addressInApp = user.getAddresses();
       log.debug("Address in application : {}",addressInApp);
       user.getAddresses().removeIf(addressInDB -> addressInDB.getAddressId().equals(addressId));
       user.getAddresses().add(updatedAddress);
       appUserRepo.save(user);
        return mapper.map(updatedAddress,AddressDTO.class);
    }
}
