package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.OrderCreationRequest;
import com.ecommerce_app.dto.request.OrderUpdateRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    /**
     * Create a new order
     *
     * @param request The order creation request
     * @return The created order response
     */
    OrderResponse createOrder(OrderCreationRequest request);

    /**
     * Get an order by its ID
     *
     * @param id The order ID
     * @return The order response
     */
    OrderResponse getOrderById(UUID id);

    /**
     * Get an order by its order number
     *
     * @param orderNumber The order number
     * @return The order response
     */
    OrderResponse getOrderByOrderNumber(String orderNumber);

    /**
     * Update an existing order
     *
     * @param id The order ID
     * @param request The order update request
     * @return The updated order response
     */
    OrderResponse updateOrder(UUID id, OrderUpdateRequest request);

    /**
     * Delete an order by its ID
     *
     * @param id The order ID
     */
    void deleteOrder(UUID id);

    /**
     * Get all orders with pagination
     *
     * @param pageable Pagination information
     * @return Page of order responses
     */
    Page<OrderResponse> getAllOrders(Pageable pageable);

    /**
     * Get orders for a specific user with pagination
     *
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of order responses
     */
    Page<OrderResponse> getOrdersByUser(UUID userId, Pageable pageable);

    /**
     * Get orders by status with pagination
     *
     * @param statusId The status ID
     * @param pageable Pagination information
     * @return Page of order responses
     */
    Page<OrderResponse> getOrdersByStatus(UUID statusId, Pageable pageable);

    /**
     * Get orders for a specific user with a specific status
     *
     * @param userId The user ID
     * @param statusId The status ID
     * @param pageable Pagination information
     * @return Page of order responses
     */
    Page<OrderResponse> getOrdersByUserAndStatus(UUID userId, UUID statusId, Pageable pageable);

    /**
     * Get orders created within a date range
     *
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of order responses
     */
    List<OrderResponse> getOrdersInDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Update the status of an order
     *
     * @param id The order ID
     * @param statusId The new status ID
     * @return The updated order response
     */
    OrderResponse updateOrderStatus(UUID id, UUID statusId);

    /**
     * Cancel an order
     *
     * @param id The order ID
     * @return The updated order response
     */
    OrderResponse cancelOrder(UUID id);

    /**
     * Complete an order
     *
     * @param id The order ID
     * @return The updated order response
     */
    OrderResponse completeOrder(UUID id);
}
