package com.ecommerce.manideep.sb.ecom.exeptions;

public class ResourseNotFoundExeption extends RuntimeException {
    String resourseName ;
    String field ;
    String fieldName;
    Long fieldId ;

    public ResourseNotFoundExeption() {
    }

    public ResourseNotFoundExeption(String message, String resourseName, String field, String fieldName) {
        super(String.format("%s is not found with %s : %s" ,resourseName , field,fieldName ));
        this.resourseName = resourseName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourseNotFoundExeption(String resourseName, String field, Long fieldId) {
        super(String.format("%s is not found with %s : %d" ,resourseName , field,fieldId ));
        this.resourseName = resourseName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
