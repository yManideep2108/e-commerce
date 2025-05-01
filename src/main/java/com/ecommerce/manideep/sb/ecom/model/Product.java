package com.ecommerce.manideep.sb.ecom.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Long productId;
    @NotBlank
    @Size(min = 3 ,message = "Product name must contain atleast 3 charecters")
    private String productName;
    @NotBlank
    @Size(min = 6 ,message = "Description must contain atleast 3 charecters")
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
