package com.zorvyn.financedashboard.exception;

import com.zorvyn.financedashboard.dtos.ErrorResponseDto;
import com.zorvyn.financedashboard.exception.custom.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthException(AuthException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExistsException(UserAlreadyExistsException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoleNotFoundException(RoleNotFoundException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryExistsException(ResourceAlreadyExistsException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleCategoryNotFoundException(ResourceNotFoundException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException cause, WebRequest request){
        return ResponseEntity
                .status(cause.getResponseStatus().getStatusCode())
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        cause.getResponseStatus(),
                        cause.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException cause, WebRequest request) {
        String errorMsg = cause.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(
                        request.getDescription(false),
                        ResponseStatus.INVALID_ARGUMENT,
                        errorMsg
                ));
    }

}
