package com.zorvyn.financedashboard.exception.custom;

import com.zorvyn.financedashboard.exception.ResponseStatus;

public class ResourceNotFoundException extends RuntimeException{

    private final ResponseStatus responseStatus;

    public ResourceNotFoundException(String message, ResponseStatus status){
        super(message);
        this.responseStatus = status;
    }

    public ResponseStatus getResponseStatus(){
        return responseStatus;
    }

}
