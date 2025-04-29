package com.ecommerce.manideep.sb.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productName;
    private String description;
    private Integer quantity;
    private double price;
    private String image;
    private double specialPrice;
    private double discount ;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;
}
