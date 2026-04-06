package com.zorvyn.financedashboard.exception.custom;

import com.zorvyn.financedashboard.exception.ResponseStatus;

public class AuthException extends RuntimeException{

    private final ResponseStatus responseStatus;

    public AuthException(String message, ResponseStatus status, Throwable cause){
        super(message, cause);
        this.responseStatus = status;
    }

    public AuthException(String message, ResponseStatus status){
        super(message);
        this.responseStatus = status;
    }

    public ResponseStatus getResponseStatus(){
        return responseStatus;
    }

}
