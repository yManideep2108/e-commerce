package com.ecommerce.manideep.sb.ecom.exeptions;

public class APIExeption extends RuntimeException{
    private static final long serialVersionUID =1L ;

    public APIExeption() {
    }

    public APIExeption(String message) {
        super(message);
    }
}
