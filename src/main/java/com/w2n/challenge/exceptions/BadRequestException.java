package com.w2n.challenge.exceptions;

public class BadRequestException extends ApiException{
    public BadRequestException(String message) {
        super(message);
    }
}
