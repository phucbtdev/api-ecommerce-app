package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderItemUpdateRequest;
import com.ecommerce_app.dto.response.OrderItemResponse;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    /**
     * Create a new order item
     *
     * @param orderId The order ID
     * @param request The order item creation request
     * @return The created order item response
     */
    OrderItemResponse createOrderItem(UUID orderId, OrderItemCreationRequest request);

    /**
     * Create multiple order items at once
     *
     * @param orderId The order ID
     * @param requests List of order item creation requests
     * @return List of created order item responses
     */
    List<OrderItemResponse> createOrderItems(UUID orderId, List<OrderItemCreationRequest> requests);

    /**
     * Get an order item by its ID
     *
     * @param id The order item ID
     * @return The order item response
     */
    OrderItemResponse getOrderItemById(UUID id);

    /**
     * Get all order items for a specific order
     *
     * @param orderId The order ID
     * @return List of order item responses
     */
    List<OrderItemResponse> getOrderItemsByOrderId(UUID orderId);

    /**
     * Update an existing order item
     *
     * @param id The order item ID
     * @param request The order item update request
     * @return The updated order item response
     */
    OrderItemResponse updateOrderItem(UUID id, OrderItemUpdateRequest request);

    /**
     * Delete an order item by its ID
     *
     * @param id The order item ID
     */
    void deleteOrderItem(UUID id);

    /**
     * Delete all order items for a specific order
     *
     * @param orderId The order ID
     */
    void deleteOrderItemsByOrderId(UUID orderId);
}
