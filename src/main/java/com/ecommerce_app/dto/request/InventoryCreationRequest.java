package com.ecommerce_app.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.*;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryCreationRequest {
    @NotNull(message = "Product variant ID is required")
    UUID productVariantId;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    Integer stockQuantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    Integer reservedQuantity;

    @Min(value = 0, message = "Reorder level cannot be negative")
    Integer reorderLevel;

    String sku;

    String location;
}
