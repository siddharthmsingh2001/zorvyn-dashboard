package com.zorvyn.financedashboard.dtos;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class APIResponse<T> implements Serializable {

    private final String status;
    private final int statusCode;
    private final String message;
    private final LocalDateTime time;
    private final T data;

    private APIResponse(HttpStatus httpStatus, String message, T data){
        this.status = httpStatus.name();
        this.statusCode = httpStatus.value();
        this.message = message;
        this.time = LocalDateTime.now();
        this.data = data;
    }

    public static <T> APIResponse<T> ok(T data, String message){
        return new APIResponse<>(HttpStatus.OK, message, data);
    }

    public static <T> APIResponse<T> created(T data, String message) {
        return new APIResponse<>(HttpStatus.CREATED, message, data);
    }

    public static <T> APIResponse<T> of(HttpStatus status, T data, String message) {
        return new APIResponse<>(status, message, data);
    }
}
