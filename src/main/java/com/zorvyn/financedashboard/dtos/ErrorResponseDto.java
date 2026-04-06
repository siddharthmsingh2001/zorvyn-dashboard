package com.zorvyn.financedashboard.dtos;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import java.time.LocalDateTime;

@Getter
@ToString
public class ErrorResponseDto {

    private final String apiPath;
    private final int statusCode;
    private final HttpStatus errorStatus;
    private final String errorCode;
    private final LocalDateTime errorTime;
    private final String causeMsg;

    public ErrorResponseDto(String apiPath, ResponseStatus responseStatus, String causeMsg){
        this.apiPath = apiPath;
        this.statusCode = responseStatus.getStatusCode();
        this.errorStatus = responseStatus.getStatusMsg();
        this.errorCode = responseStatus.name();
        this.errorTime = LocalDateTime.now();
        this.causeMsg = causeMsg;
    }

}
