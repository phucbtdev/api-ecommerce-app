package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionCreationRequest {
    @NotNull(message = "Inventory ID is required")
    private UUID inventoryId;

    @NotBlank(message = "Transaction type is required")
    private String transactionType; // STOCK_IN, STOCK_OUT, ADJUSTMENT, RESERVATION, RELEASE_RESERVATION

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String reference; // Order ID, Purchase Order ID, etc.

    private String notes;

    private UUID createdBy;
}
