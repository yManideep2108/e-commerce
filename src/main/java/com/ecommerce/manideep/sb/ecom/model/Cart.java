package com.ecommerce.manideep.sb.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId ;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user ;

    @OneToMany(mappedBy = "cart" ,cascade = {CascadeType.MERGE,CascadeType.MERGE,CascadeType.REMOVE}
            ,orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();
    private Double totalPrice = 0.0;
}
