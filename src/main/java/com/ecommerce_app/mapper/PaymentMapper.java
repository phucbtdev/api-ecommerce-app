package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.PaymentRequest;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse paymentToPaymentResponse(Payment payment);

    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "failureReason", ignore = true)
    Payment paymentRequestToPaymentEntity(PaymentRequest request);

    List<PaymentResponse> paymentsToPaymentResponsesList(List<Payment> payments);
}
