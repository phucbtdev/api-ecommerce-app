package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.PaymentRequest;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.Payment;
import com.ecommerce_app.service.interfaces.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest
    ) {

        PaymentResponse response = paymentService.createPayment(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/number/{paymentNumber}")
    public ResponseEntity<PaymentResponse> getPaymentByPaymentNumber(
            @PathVariable String paymentNumber
    ) {

        return ResponseEntity.ok(paymentService.getPaymentByPaymentNumber(paymentNumber));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<PaymentResponse> getPaymentByReference(
            @PathVariable String reference
    ) {

        PaymentResponse response = paymentService.getPaymentByReference(reference);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(
            @PathVariable Order orderId
    ) {

        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByStatus(
            @PathVariable Payment.PaymentStatus status
    ) {

        return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable Long id
    ) {

        PaymentResponse response = paymentService.processPayment(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(
             @PathVariable Long id
    ) {

        PaymentResponse response = paymentService.cancelPayment(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable Long id
    ) {

        PaymentResponse response = paymentService.refundPayment(id);
        return ResponseEntity.ok(response);
    }
}