package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.PaymentUpdateRequest;
import com.ecommerce_app.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentCreationRequest paymentCreationRequest);

    PaymentResponse updatePayment(Long id, PaymentUpdateRequest paymentUpdateRequest);

    PaymentResponse getPaymentById(Long id);

    PaymentResponse getPaymentByOrderId(Long orderId);

    List<PaymentResponse> getAllPayments();

    void deletePayment(Long id);

}
