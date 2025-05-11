package com.ecommerce_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreationRequest {

    Long orderId;
    String paymentMethod;
    String paymentStatus;
    String transactionId;
    BigDecimal amount;
    String currency;
    String paymentDetails;
    LocalDateTime paymentDate;
}