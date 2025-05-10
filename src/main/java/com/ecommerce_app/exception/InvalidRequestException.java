package com.ecommerce_app.exception;

import lombok.Getter;

public class InvalidRequestException extends RuntimeException {
    @Getter
    private final String field;

    public InvalidRequestException(String message, String field) {
        super(message);
        this.field = field;
    }

    public InvalidRequestException(String message) {
        super(message);
        this.field = null;
    }
}
