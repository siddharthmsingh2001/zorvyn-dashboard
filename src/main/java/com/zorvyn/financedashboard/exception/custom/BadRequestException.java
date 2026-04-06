package com.zorvyn.financedashboard.exception.custom;

import com.zorvyn.financedashboard.exception.ResponseStatus;

public class BadRequestException extends RuntimeException{

    private final ResponseStatus responseStatus;

    public BadRequestException(String message, ResponseStatus status){
        super(message);
        this.responseStatus = status;
    }

    public ResponseStatus getResponseStatus(){
        return responseStatus;
    }

}
