package com.w2n.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiExceptionResponse> notFoundExceptionHandler(NotFoundException exception) {
        return this.registerException(
                exception,
                HttpStatus.NOT_FOUND
        );
    }

    private ResponseEntity<ApiExceptionResponse> registerException(
            Exception exception,
            HttpStatus httpStatus
    ) {

        exception.printStackTrace();

        ApiExceptionResponse error = new ApiExceptionResponse(httpStatus, exception.getMessage());

        return new ResponseEntity<>(error, httpStatus);
    }
}
