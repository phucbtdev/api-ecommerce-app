/**
 * Service interface that manages payment operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * payment records. It handles payment processing and information management for orders.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.PaymentCreationRequest;
import com.ecommerce_app.dto.request.PaymentUpdateRequest;
import com.ecommerce_app.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    /**
     * Creates a new payment record in the system.
     *
     * @param paymentCreationRequest The {@link PaymentCreationRequest} containing payment details
     * @return {@link PaymentResponse} representing the created payment
     */
    PaymentResponse createPayment(PaymentCreationRequest paymentCreationRequest);

    /**
     * Updates an existing payment record.
     *
     * @param id The UUID of the payment to update
     * @param paymentUpdateRequest The {@link PaymentUpdateRequest} containing updated payment details
     * @return {@link PaymentResponse} representing the updated payment
     */
    PaymentResponse updatePayment(UUID id, PaymentUpdateRequest paymentUpdateRequest);

    /**
     * Retrieves a payment by its unique identifier.
     *
     * @param id The UUID of the payment to retrieve
     * @return {@link PaymentResponse} containing the requested payment details
     */
    PaymentResponse getPaymentById(UUID id);

    /**
     * Retrieves a payment associated with a specific order.
     *
     * @param orderId The UUID of the order to get payment for
     * @return {@link PaymentResponse} containing the payment details for the specified order
     */
    PaymentResponse getPaymentByOrderId(UUID orderId);

    /**
     * Retrieves all payments in the system.
     *
     * @return A list of {@link PaymentResponse} objects representing all payments
     */
    List<PaymentResponse> getAllPayments();

    /**
     * Deletes a payment record from the system.
     *
     * @param id The UUID of the payment to delete
     */
    void deletePayment(UUID id);
}