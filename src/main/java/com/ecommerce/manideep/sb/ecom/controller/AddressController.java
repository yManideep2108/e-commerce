package com.ecommerce.manideep.sb.ecom.controller;

import com.ecommerce.manideep.sb.ecom.model.AppUser;
import com.ecommerce.manideep.sb.ecom.payload.AddressDTO;
import com.ecommerce.manideep.sb.ecom.service.AddressService;
import com.ecommerce.manideep.sb.ecom.utilities.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        AppUser user = authUtil.loggedInUser();
        AddressDTO savedAddress = addressService.createAddress(addressDTO ,user);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAddresses(){

        List<AddressDTO> addressList = addressService.getAddresses();
        return new ResponseEntity<>(addressList,HttpStatus.OK);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> getUserAddressById(@PathVariable Long addressId){

        AddressDTO address = addressService.getAddressById(addressId);
        return new ResponseEntity<>(address,HttpStatus.OK);
    }

    @GetMapping("users/address")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(){
        AppUser user = authUtil.loggedInUser();
        List<AddressDTO> addressList = addressService.getAllAddressesOfUser(user);
        return new ResponseEntity<>(addressList,HttpStatus.OK);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDTO> UpdateUserAddressById(@PathVariable Long addressId,
                                                            @RequestBody AddressDTO addressDTO){
        AppUser user = authUtil.loggedInUser();
        AddressDTO updatedAddress = addressService.updateUserAddressById(user,addressId,addressDTO);
        return new ResponseEntity<>(updatedAddress,HttpStatus.OK);
    }
}
