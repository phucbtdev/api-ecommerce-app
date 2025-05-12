package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {

    @NotNull(message = "Order status ID is required")
    private UUID statusId;

    @Size(max = 1000, message = "Customer notes cannot exceed 1000 characters")
    private String customerNotes;

    private UUID billingAddressId;

    private UUID shippingAddressId;

    // We don't allow changing order items directly, must be done through dedicated endpoints
}