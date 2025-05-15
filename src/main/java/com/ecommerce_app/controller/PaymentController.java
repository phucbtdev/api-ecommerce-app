package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.PaymentUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.PaymentResponse;
import com.ecommerce_app.service.interfaces.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing payment operations.
 * Provides endpoints for creating, updating, retrieving, and deleting payment information.
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "API for payment management")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Creates a new payment.
     *
     * @param paymentCreationRequest The payment details to create
     * @return ApiResult containing the created payment information
     */
    @PostMapping
    @Operation(summary = "Create a new payment", description = "Creates a new payment record with the provided details")
    public ApiResult<PaymentResponse> createPayment(@RequestBody @Valid PaymentCreationRequest paymentCreationRequest) {
        PaymentResponse payment = paymentService.createPayment(paymentCreationRequest);
        return ApiResult.success("Payment created successfully", payment);
    }

    /**
     * Updates an existing payment.
     *
     * @param id The ID of the payment to update
     * @param paymentUpdateRequest The payment details to update
     * @return ApiResult containing the updated payment information
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing payment", description = "Updates a payment with the provided details")
    public ApiResult<PaymentResponse> updatePayment(@PathVariable UUID id,
                                                    @RequestBody @Valid PaymentUpdateRequest paymentUpdateRequest) {
        PaymentResponse payment = paymentService.updatePayment(id, paymentUpdateRequest);
        return ApiResult.success("Payment updated successfully", payment);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id The ID of the payment to retrieve
     * @return ApiResult containing the payment information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieves payment information for the specified ID")
    public ApiResult<PaymentResponse> getPaymentById(@PathVariable UUID id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ApiResult.success("Payment retrieved successfully", payment);
    }

    /**
     * Retrieves a payment by its associated order ID.
     *
     * @param orderId The ID of the order associated with the payment
     * @return ApiResult containing the payment information
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Retrieves payment information for the specified order")
    public ApiResult<PaymentResponse> getPaymentByOrderId(@PathVariable UUID orderId) {
        PaymentResponse payment = paymentService.getPaymentByOrderId(orderId);
        return ApiResult.success("Payment retrieved successfully", payment);
    }

    /**
     * Retrieves all payments.
     *
     * @return ApiResult containing a list of all payments
     */
    @GetMapping
    @Operation(summary = "Get all payments", description = "Retrieves a list of all payment records")
    public ApiResult<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ApiResult.success("Payments retrieved successfully", payments);
    }

    /**
     * Deletes a payment.
     *
     * @param id The ID of the payment to delete
     * @return ApiResult with a success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a payment", description = "Deletes the payment with the specified ID")
    public ApiResult<Void> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);
        return ApiResult.success("Payment deleted successfully", null);
    }
}