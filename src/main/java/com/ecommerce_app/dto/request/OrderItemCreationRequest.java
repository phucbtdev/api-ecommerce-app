package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemCreationRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    private UUID productVariantId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Size(max = 500, message = "Variant info cannot exceed 500 characters")
    private String variantInfo;

    // We'll calculate unit price and total price based on the product and variant in the service
}
