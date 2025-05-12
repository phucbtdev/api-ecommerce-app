package com.ecommerce_app.dto.response;

import com.ecommerce_app.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;
    private String orderNumber;
    private UUID userId;
    private String userEmail; // For convenience
    private String userName; // Concatenated first and last name

    private BigDecimal totalAmount;
    private BigDecimal shippingAmount;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private String customerNotes;

    private Set<OrderItemResponse> orderItems = new HashSet<>();

    private PaymentResponse payment;
    private ShippingResponse shipping;

    private String orderStatus;
    private UUID orderStatusId;

    private AddressResponse billingAddress;
    private AddressResponse shippingAddress;

    private CouponResponse coupon;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
