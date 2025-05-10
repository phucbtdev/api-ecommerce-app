package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.PaymentRequest;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;

import java.util.List;

public interface PaymentService {

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByPaymentNumber(String paymentNumber);

    PaymentResponse getPaymentByOrderId(Order orderId);

    List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status);

    PaymentResponse createPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentByReference(String reference);

    List<PaymentResponse> getPaymentsByOrderId(Long orderId);

    PaymentResponse processPayment(Long id);

    PaymentResponse cancelPayment(Long id);

    PaymentResponse refundPayment(Long id);

}
