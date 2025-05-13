package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.PaymentUpdateRequest;
import com.ecommerce_app.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentCreationRequest paymentCreationRequest);

    PaymentResponse updatePayment(UUID id, PaymentUpdateRequest paymentUpdateRequest);

    PaymentResponse getPaymentById(UUID id);

    PaymentResponse getPaymentByOrderId(UUID orderId);

    List<PaymentResponse> getAllPayments();

    void deletePayment(UUID id);

}
