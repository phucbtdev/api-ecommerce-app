package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryUpdateRequest {
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity;

    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    private String sku;

    private String location;
}
