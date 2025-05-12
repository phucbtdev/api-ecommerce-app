package com.ecommerce_app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreationRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @Valid
    @NotEmpty(message = "Order must contain at least one item")
    private Set<OrderItemCreationRequest> orderItems = new HashSet<>();

    @Size(max = 1000, message = "Customer notes cannot exceed 1000 characters")
    private String customerNotes;

    private UUID couponId;

    @NotNull(message = "Billing address ID is required")
    private UUID billingAddressId;

    @NotNull(message = "Shipping address ID is required")
    private UUID shippingAddressId;

    @NotNull(message = "Shipping method is required")
    @NotBlank(message = "Shipping method cannot be blank")
    private String shippingMethod;

    @NotNull(message = "Payment method is required")
    @NotBlank(message = "Payment method cannot be blank")
    private String paymentMethod;

    // Payment details if needed
    private String paymentDetails;
}
