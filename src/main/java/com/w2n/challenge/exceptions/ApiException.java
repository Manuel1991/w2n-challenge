package com.w2n.challenge.exceptions;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

    private final String message;

    public ApiException(String message) {
        this.message = message;
    }
}
