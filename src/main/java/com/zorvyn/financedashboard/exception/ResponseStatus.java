package com.zorvyn.financedashboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseStatus {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN),

    USER_LOCKED(HttpStatus.LOCKED.value(), HttpStatus.LOCKED),
    USER_DISABLED(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),
    USER_DELETED(HttpStatus.GONE.value(), HttpStatus.GONE),
    USER_EXISTS(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT),

    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),

    CATEGORY_EXISTS(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),

    RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND),

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);

    private final int statusCode;
    private final HttpStatus statusMsg;

    ResponseStatus(int statusCode, HttpStatus statusMsg){
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

}