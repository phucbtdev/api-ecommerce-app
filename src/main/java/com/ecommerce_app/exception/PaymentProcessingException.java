package com.ecommerce_app.exception;

import lombok.Getter;

public class PaymentProcessingException extends RuntimeException {
    @Getter
    private final String errorCode;

    public PaymentProcessingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PaymentProcessingException(String message) {
        super(message);
        this.errorCode = "PAYMENT_PROCESSING_ERROR";
    }
}
