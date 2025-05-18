package com.ecommerce.manideep.sb.ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId ;

    @NotBlank
    @Size(min = 5, message = "Building name must be atleast 5 characters")
    private String street ;

    @NotBlank
    @Size(min = 3, message = "Building name must be atleast 5 characters")
    private String buildingName ;

    @NotBlank
    @Size(min = 4, message = "city must be atleast 5 characters")
    private String city ;

    @NotBlank
    @Size(min = 2, message = "state must be atleast 5 characters")
    private String state ;

    @NotBlank
    @Size(min = 2, message = "country must be atleast 5 characters")
    private  String country ;
    @NotBlank
    @Size(min = 5, message = "pincode must be atleast 5 characters")
    private String pincode ;

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user ;
}
