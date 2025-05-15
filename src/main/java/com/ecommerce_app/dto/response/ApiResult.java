package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic API response wrapper for standardized API responses.
 *
 * @param <T> The type of data contained in the response
 */
@Schema(description = "Standard API response format")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    @Schema(description = "Indicates whether the operation was successful")
    private boolean success;

    @Schema(description = "Message providing information about the operation result")
    private String message;

    @Schema(description = "The payload data returned by the operation")
    private T data;

    /**
     * Creates a successful API result.
     *
     * @param <T> Type of data contained in the response
     * @param message Success message
     * @param data The payload data
     * @return A new ApiResult instance representing success
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return ApiResult.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates an error API result.
     *
     * @param <T> Type of data contained in the response
     * @param message Error message
     * @param data Optional error data (can be null)
     * @return A new ApiResult instance representing an error
     */
    public static <T> ApiResult<T> error(String message, T data) {
        return ApiResult.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
}
