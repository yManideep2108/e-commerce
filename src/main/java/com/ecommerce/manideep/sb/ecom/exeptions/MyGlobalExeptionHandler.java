package com.ecommerce.manideep.sb.ecom.exeptions;

import com.ecommerce.manideep.sb.ecom.payload.APIResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice

public class MyGlobalExeptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidExeption(MethodArgumentNotValidException e){
        Map<String,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String errorMessage = err.getDefaultMessage();
            response.put(fieldName ,errorMessage );
        });
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(ResourseNotFoundExeption.class)
    public ResponseEntity<APIResponseStatus> myResourseNotFoundExeption(ResourseNotFoundExeption resourseNotFoundExeption){
        APIResponseStatus apiResponseStatus = new APIResponseStatus();
        apiResponseStatus.message =  resourseNotFoundExeption.getMessage();
        apiResponseStatus.setStatus(false);
        return new ResponseEntity<>(apiResponseStatus,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIExeption.class)
    public ResponseEntity<APIResponseStatus> myAPIExeption(APIExeption apiExeption){
        APIResponseStatus apiResponseStatus = new APIResponseStatus();
        apiResponseStatus.message =  apiExeption.getMessage();
        return new ResponseEntity<>(apiResponseStatus,HttpStatus.BAD_REQUEST);
    }
}
