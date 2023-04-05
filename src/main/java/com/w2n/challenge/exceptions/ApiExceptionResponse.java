package com.w2n.challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiExceptionResponse {
    private HttpStatus status;
    private String message;
}
