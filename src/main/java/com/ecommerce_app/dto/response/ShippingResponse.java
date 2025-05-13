package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingResponse {
    UUID id;
    UUID orderId;
    String orderNumber;
    String shippingMethod;
    BigDecimal shippingCost;
    String trackingNumber;
    String carrier;
    LocalDateTime estimatedDeliveryDate;
    LocalDateTime actualDeliveryDate;
    String shippingStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
