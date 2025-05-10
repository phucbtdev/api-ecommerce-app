package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.PaymentRequest;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import com.ecommerce_app.exception.EntityNotFoundException;
import com.ecommerce_app.exception.InvalidRequestException;
import com.ecommerce_app.exception.PaymentProcessingException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.PaymentMapper;
import com.ecommerce_app.repository.PaymentRepository;
import com.ecommerce_app.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        log.info("Fetching payment with id: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        return paymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByPaymentNumber(String paymentNumber) {
        log.info("Fetching payment with payment number: {}", paymentNumber);
        Payment payment = paymentRepository.findByPaymentNumber(paymentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "paymentNumber", paymentNumber));
        return paymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Order orderId) {
        log.info("Fetching payment for order id: {}", orderId);
        Payment payment = paymentRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));
        return paymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        log.info("Creating payment for order: {}", paymentRequest.getOrderId());

        // Generate a unique payment reference if not provided
        if (paymentRequest.getPaymentNumber() == null || paymentRequest.getPaymentNumber().isBlank()) {
            paymentRequest.setPaymentNumber(generatePaymentReference());
        }

        // Validate payment request
        validatePaymentRequest(paymentRequest);

        // Map request to entity
        Payment payment = paymentMapper.paymentRequestToPaymentEntity(paymentRequest);

        // Save payment
        payment = paymentRepository.save(payment);
        log.info("Payment created successfully with ID: {}", payment.getId());

        return paymentMapper.paymentToPaymentResponse(payment);
    }


    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByReference(String reference) {
        log.info("Fetching payment with reference: {}", reference);
        Payment payment = paymentRepository.findByPaymentNumber(reference)
                .orElseThrow(() -> new EntityNotFoundException("Payment", "reference", reference));
        return paymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return List.of();
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
//        log.info("Fetching payments for order ID: {}", orderId);
//        List<Payment> payments = paymentRepository.findByOrderId(orderId);
//        return paymentMapper.paymentsToPaymentResponsesList(payments);
//    }

    @Override
    @Transactional
    public PaymentResponse processPayment(Long id) {
        log.info("Processing payment with ID: {}", id);
        Payment payment = getPaymentEntityById(id);

        // Check if payment can be processed
        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new InvalidRequestException("Payment can only be processed when in PENDING status", "status");
        }

        try {
            // Simulate payment gateway processing
            boolean isSuccessful = processPaymentWithGateway(payment);

            if (isSuccessful) {
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                payment.setProcessedAt(LocalDateTime.now());
                payment.setTransactionId(generateTransactionId());
            } else {
                payment.setStatus(Payment.PaymentStatus.FAILED);
                payment.setFailureReason("Payment gateway declined the transaction");
            }

            payment = paymentRepository.save(payment);
            log.info("Payment processed successfully with status: {}", payment.getStatus());

            return paymentMapper.paymentToPaymentResponse(payment);
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Internal error: " + e.getMessage());
            payment = paymentRepository.save(payment);

            throw new PaymentProcessingException("Failed to process payment: " + e.getMessage(), "PAYMENT_GATEWAY_ERROR");
        }
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(Long id) {
        log.info("Cancelling payment with ID: {}", id);
        Payment payment = getPaymentEntityById(id);

        // Check if payment can be cancelled
        if (payment.getStatus() != Payment.PaymentStatus.PENDING && payment.getStatus() != Payment.PaymentStatus.PROCESSING) {
            throw new InvalidRequestException("Payment can only be cancelled when in PENDING or PROCESSING status", "status");
        }

        payment.setStatus(Payment.PaymentStatus.CANCELLED);
        payment = paymentRepository.save(payment);
        log.info("Payment cancelled successfully");

        return paymentMapper.paymentToPaymentResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse refundPayment(Long id) {
        log.info("Refunding payment with ID: {}", id);
        Payment payment = getPaymentEntityById(id);

        // Check if payment can be refunded
        if (payment.getStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new InvalidRequestException("Payment can only be refunded when in COMPLETED status", "status");
        }

        try {
            // Simulate refund processing with payment gateway
            boolean isRefundSuccessful = processRefundWithGateway(payment);

            if (isRefundSuccessful) {
                payment.setStatus(Payment.PaymentStatus.REFUNDED);
                payment = paymentRepository.save(payment);
                log.info("Payment refunded successfully");
            } else {
                throw new PaymentProcessingException("Failed to process refund with payment gateway", "REFUND_FAILED");
            }

            return paymentMapper.paymentToPaymentResponse(payment);
        } catch (Exception e) {
            log.error("Error refunding payment: {}", e.getMessage(), e);
            throw new PaymentProcessingException("Failed to refund payment: " + e.getMessage(), "REFUND_ERROR");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status) {
        log.info("Fetching payments with status: {}", status);
        List<Payment> payments = paymentRepository.findByStatus(status);
        return paymentMapper.paymentsToPaymentResponsesList(payments);
    }



    // Helper methods
    private Payment getPaymentEntityById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment", "id", id));
    }

    private void validatePaymentRequest(PaymentRequest request) {
        // Additional validation logic can be added here
        if (request.getAmount().doubleValue() <= 0) {
            throw new InvalidRequestException("Payment amount must be greater than zero", "amount");
        }

        // Validate payment method specific fields
        switch (request.getMethod()) {
            case CREDIT_CARD, DEBIT_CARD -> {
                if (request.getCardNumber() == null || request.getCardNumber().isBlank()) {
                    throw new InvalidRequestException("Card number is required for card payments", "cardNumber");
                }
                if (request.getExpiryDate() == null || request.getExpiryDate().isBlank()) {
                    throw new InvalidRequestException("Expiry date is required for card payments", "expiryDate");
                }
                if (request.getCvv() == null || request.getCvv().isBlank()) {
                    throw new InvalidRequestException("CVV is required for card payments", "cvv");
                }
            }
            case PAYPAL -> {
                if (request.getPaypalEmail() == null || request.getPaypalEmail().isBlank()) {
                    throw new InvalidRequestException("PayPal email is required for PayPal payments", "paypalEmail");
                }
            }
            case BANK_TRANSFER -> {
                if (request.getBankAccountNumber() == null || request.getBankAccountNumber().isBlank()) {
                    throw new InvalidRequestException("Bank account number is required for bank transfers", "bankAccountNumber");
                }
                if (request.getBankCode() == null || request.getBankCode().isBlank()) {
                    throw new InvalidRequestException("Bank code is required for bank transfers", "bankCode");
                }
            }
            // Add cases for other payment methods as needed
        }
    }

    private String generatePaymentReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    // Mock payment gateway processing - in a real application, this would integrate with a payment provider API
    private boolean processPaymentWithGateway(Payment payment) {
        // Simulate payment processing
        log.info("Processing payment with payment gateway for payment ID: {}", payment.getId());

        // For demo purposes, simulate a success rate of 90%
        return Math.random() < 0.9;
    }

    // Mock refund processing with payment gateway
    private boolean processRefundWithGateway(Payment payment) {
        // Simulate refund processing
        log.info("Processing refund with payment gateway for payment ID: {}", payment.getId());

        // For demo purposes, simulate a success rate of 95%
        return Math.random() < 0.95;
    }
}
