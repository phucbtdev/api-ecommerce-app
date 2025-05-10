package com.ecommerce_app.dto.response;

import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private String paymentNumber;
    private Long orderId;
    private BigDecimal amount;
    private Payment.PaymentStatus status;
    private Payment.PaymentMethod method;
    private String transactionId;
    private LocalDateTime processedAt;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String additionalDetails;
}
