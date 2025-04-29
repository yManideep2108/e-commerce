package com.ecommerce.manideep.sb.ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private String name ;
    private Integer quantity ;
    private double price;
    private double discount ;
    private double specialPrice ;
    private String description ;
}
