package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {

    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResult<T> success(String message, T data) {
        return ApiResult.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResult<T> error(String message, T data) {
        return ApiResult.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}
