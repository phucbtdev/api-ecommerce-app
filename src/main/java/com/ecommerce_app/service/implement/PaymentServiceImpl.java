package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.PaymentUpdateRequest;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.PaymentMapper;
import com.ecommerce_app.repository.OrderRepository;
import com.ecommerce_app.repository.PaymentRepository;
import com.ecommerce_app.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentCreationRequest paymentCreationRequest) {
        Order order = orderRepository.findById(paymentCreationRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentCreationRequest.getOrderId()));

        Payment payment = paymentMapper.toEntity(paymentCreationRequest, order);
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse updatePayment(UUID id, PaymentUpdateRequest paymentUpdateRequest) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        paymentMapper.updatePaymentFromDto(paymentUpdateRequest, payment);
        Payment updatedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(updatedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for order id: " + orderId));

        return paymentMapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePayment(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }

        paymentRepository.deleteById(id);
    }
}