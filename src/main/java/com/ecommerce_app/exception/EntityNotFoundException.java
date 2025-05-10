package com.ecommerce_app.exception;


import lombok.Getter;

public class EntityNotFoundException extends RuntimeException {
    @Getter
    private final String entityName;
    @Getter
    private final String fieldName;
    @Getter
    private final Object fieldValue;

    public EntityNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

