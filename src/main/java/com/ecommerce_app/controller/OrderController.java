package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.OrderCreationRequest;
import com.ecommerce_app.dto.request.OrderUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing orders in the e-commerce application.
 * Provides endpoints for creating, retrieving, updating, and deleting orders,
 * as well as filtering orders by various criteria.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for order operations")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order in the system.
     *
     * @param request DTO containing order creation details
     * @return ApiResult containing the created OrderResponse
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new order", description = "Creates a new order with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ApiResult<OrderResponse> createOrder(@Valid @RequestBody OrderCreationRequest request) {
        return ApiResult.success("Order created successfully", orderService.createOrder(request));
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id UUID of the order to retrieve
     * @return ApiResult containing the OrderResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get order by ID", description = "Retrieves an order using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderResponse> getOrderById(@PathVariable UUID id) {
        return ApiResult.success("Order retrieved successfully", orderService.getOrderById(id));
    }

    /**
     * Retrieves an order by its order number.
     *
     * @param orderNumber The order number to search for
     * @return ApiResult containing the OrderResponse
     */
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get order by order number", description = "Retrieves an order using its order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderResponse> getOrderByOrderNumber(@PathVariable String orderNumber) {
        return ApiResult.success("Order retrieved successfully", orderService.getOrderByOrderNumber(orderNumber));
    }

    /**
     * Updates an existing order.
     *
     * @param id UUID of the order to update
     * @param request DTO containing order update details
     * @return ApiResult containing the updated OrderResponse
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Update an order", description = "Updates an existing order with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderResponse> updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequest request) {
        return ApiResult.success("Order updated successfully", orderService.updateOrder(id, request));
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id UUID of the order to delete
     * @return ApiResult with no data content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ApiResult.success("Order deleted successfully", null);
    }

    /**
     * Retrieves all orders with pagination.
     *
     * @param pageable Pagination parameters
     * @return ApiResult containing a page of OrderResponse objects
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders", description = "Retrieves all orders with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ApiResult<Page<OrderResponse>> getAllOrders(Pageable pageable) {
        return ApiResult.success("Orders retrieved successfully", orderService.getAllOrders(pageable));
    }

    /**
     * Retrieves all orders for a specific user with pagination.
     *
     * @param userId UUID of the user
     * @param pageable Pagination parameters
     * @return ApiResult containing a page of OrderResponse objects
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get orders by user", description = "Retrieves all orders for a specific user with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ApiResult<Page<OrderResponse>> getOrdersByUser(
            @PathVariable UUID userId,
            Pageable pageable) {
        return ApiResult.success("User orders retrieved successfully", orderService.getOrdersByUser(userId, pageable));
    }

    /**
     * Retrieves all orders with a specific status with pagination.
     *
     * @param statusId UUID of the order status
     * @param pageable Pagination parameters
     * @return ApiResult containing a page of OrderResponse objects
     */
    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders by status", description = "Retrieves all orders with a specific status with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Status not found")
    })
    public ApiResult<Page<OrderResponse>> getOrdersByStatus(
            @PathVariable UUID statusId,
            Pageable pageable) {
        return ApiResult.success("Orders by status retrieved successfully", orderService.getOrdersByStatus(statusId, pageable));
    }

    /**
     * Retrieves all orders for a specific user with a specific status with pagination.
     *
     * @param userId UUID of the user
     * @param statusId UUID of the order status
     * @param pageable Pagination parameters
     * @return ApiResult containing a page of OrderResponse objects
     */
    @GetMapping("/user/{userId}/status/{statusId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get orders by user and status",
            description = "Retrieves all orders for a specific user with a specific status with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User or status not found")
    })
    public ApiResult<Page<OrderResponse>> getOrdersByUserAndStatus(
            @PathVariable UUID userId,
            @PathVariable UUID statusId,
            Pageable pageable) {
        return ApiResult.success("User orders by status retrieved successfully",
                orderService.getOrdersByUserAndStatus(userId, statusId, pageable));
    }

    /**
     * Retrieves all orders within a specified date range.
     *
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return ApiResult containing a list of OrderResponse objects
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders in date range",
            description = "Retrieves all orders created within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ApiResult<List<OrderResponse>> getOrdersInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ApiResult.success("Orders in date range retrieved successfully",
                orderService.getOrdersInDateRange(startDate, endDate));
    }

    /**
     * Updates the status of an order.
     *
     * @param id UUID of the order to update
     * @param statusId UUID of the new status
     * @return ApiResult containing the updated OrderResponse
     */
    @PatchMapping("/{id}/status/{statusId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status", description = "Updates the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order or status not found")
    })
    public ApiResult<OrderResponse> updateOrderStatus(
            @PathVariable UUID id,
            @PathVariable UUID statusId) {
        return ApiResult.success("Order status updated successfully", orderService.updateOrderStatus(id, statusId));
    }

    /**
     * Cancels an order.
     *
     * @param id UUID of the order to cancel
     * @return ApiResult containing the updated OrderResponse
     */
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Cancel an order", description = "Cancels an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderResponse> cancelOrder(@PathVariable UUID id) {
        return ApiResult.success("Order cancelled successfully", orderService.cancelOrder(id));
    }

    /**
     * Marks an order as complete.
     *
     * @param id UUID of the order to complete
     * @return ApiResult containing the updated OrderResponse
     */
    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Complete an order", description = "Marks an existing order as complete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order completed successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderResponse> completeOrder(@PathVariable UUID id) {
        return ApiResult.success("Order completed successfully", orderService.completeOrder(id));
    }
}