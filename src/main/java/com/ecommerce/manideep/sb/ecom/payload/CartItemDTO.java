package com.ecommerce.manideep.sb.ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemDtoId;
    private CartDTO cartDTO;
    private ProductDTO productDTO;
    private Integer quantity ;
    private  Double discount ;
    private Double  productPrice ;
}
