/**
 * Service interface that manages order status operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * order statuses within the system. Order statuses track the current state of orders
 * throughout their lifecycle.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.OrderStatusResponse;

import java.util.List;
import java.util.UUID;

public interface OrderStatusService {
    /**
     * Creates a new order status in the system.
     *
     * @param request The {@link OrderStatusCreationRequest} containing the order status details
     * @return {@link OrderStatusResponse} representing the created order status
     */
    OrderStatusResponse createOrderStatus(OrderStatusCreationRequest request);

    /**
     * Retrieves an order status by its unique identifier.
     *
     * @param id The UUID of the order status to retrieve
     * @return {@link OrderStatusResponse} containing the requested order status details
     */
    OrderStatusResponse getOrderStatus(UUID id);

    /**
     * Retrieves all order statuses available in the system.
     *
     * @return A list of {@link OrderStatusResponse} objects representing all order statuses
     */
    List<OrderStatusResponse> getAllOrderStatuses();

    /**
     * Updates an existing order status.
     *
     * @param id The UUID of the order status to update
     * @param request The {@link OrderStatusUpdateRequest} containing the updated order status details
     * @return {@link OrderStatusResponse} representing the updated order status
     */
    OrderStatusResponse updateOrderStatus(UUID id, OrderStatusUpdateRequest request);

    /**
     * Deletes an order status from the system.
     *
     * @param id The UUID of the order status to delete
     */
    void deleteOrderStatus(UUID id);
}