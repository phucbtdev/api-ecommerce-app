package com.ecommerce_app.dto.request;

import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private Payment.PaymentMethod method;

    private String paymentNumber;

    // For credit/debit card payments
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    // For PayPal
    private String paypalEmail;

    // For bank transfer
    private String bankAccountNumber;
    private String bankCode;

    // For other payment methods
    private String additionalDetails;
}