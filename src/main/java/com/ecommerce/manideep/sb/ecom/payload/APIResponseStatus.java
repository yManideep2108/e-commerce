package com.ecommerce.manideep.sb.ecom.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseStatus {
    public String message ;
    private boolean status ;

}
