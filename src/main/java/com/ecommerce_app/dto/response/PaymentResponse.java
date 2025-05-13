package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    UUID id;
    UUID orderId;
    String orderNumber;
    String paymentMethod;
    String paymentStatus;
    String transactionId;
    BigDecimal amount;
    String currency;
    String paymentDetails;
    LocalDateTime paymentDate;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
