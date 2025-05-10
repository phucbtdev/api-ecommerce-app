package com.ecommerce_app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private int status;
    private String error;
    private Map<String, String> errors;
}

