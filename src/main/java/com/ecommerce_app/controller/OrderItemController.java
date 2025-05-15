package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderItemUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.OrderItemResponse;
import com.ecommerce_app.service.interfaces.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing order items within the e-commerce application.
 * Provides endpoints for creating, retrieving, updating, and deleting order items.
 */
@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Item Management", description = "APIs for order item operations")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Creates a new order item for a specific order.
     *
     * @param orderId UUID of the order to add the item to
     * @param request DTO containing order item creation details
     * @return ApiResult containing the created OrderItemResponse
     */
    @PostMapping
    @Operation(summary = "Create an order item", description = "Creates a new order item for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order item created successfully",
                    content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<OrderItemResponse> createOrderItem(
            @PathVariable UUID orderId,
            @Valid @RequestBody OrderItemCreationRequest request) {
        log.info("Request to create order item for order ID: {}", orderId);
        OrderItemResponse response = orderItemService.createOrderItem(orderId, request);
        return ApiResult.success("Order item created successfully", response);
    }

    /**
     * Creates multiple order items for a specific order in a single request.
     *
     * @param orderId UUID of the order to add the items to
     * @param requests List of DTOs containing order item creation details
     * @return ApiResult containing a list of created OrderItemResponse objects
     */
    @PostMapping("/batch")
    @Operation(summary = "Create multiple order items",
            description = "Creates multiple order items for a specific order in a single request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order items created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<List<OrderItemResponse>> createOrderItems(
            @PathVariable UUID orderId,
            @Valid @RequestBody List<OrderItemCreationRequest> requests) {
        log.info("Request to create multiple order items for order ID: {}", orderId);
        List<OrderItemResponse> responses = orderItemService.createOrderItems(orderId, requests);
        return ApiResult.success("Order items created successfully", responses);
    }

    /**
     * Retrieves a specific order item by its ID.
     *
     * @param orderId UUID of the order
     * @param itemId UUID of the order item to retrieve
     * @return ApiResult containing the OrderItemResponse or an error if not found
     */
    @GetMapping("/{itemId}")
    @Operation(summary = "Get order item by ID",
            description = "Retrieves an order item by its ID within a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item found",
                    content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order item not found or does not belong to the order")
    })
    public ApiResult<OrderItemResponse> getOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId) {
        log.info("Request to get order item with ID: {} from order ID: {}", itemId, orderId);
        OrderItemResponse response = orderItemService.getOrderItemById(itemId);

        // Validate that item belongs to specified order
        if (!response.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ApiResult.error("Order item not found in the specified order", null);
        }

        return ApiResult.success("Order item retrieved successfully", response);
    }

    /**
     * Retrieves all order items for a specific order.
     *
     * @param orderId UUID of the order
     * @return ApiResult containing a list of OrderItemResponse objects
     */
    @GetMapping
    @Operation(summary = "Get all order items", description = "Retrieves all order items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<List<OrderItemResponse>> getOrderItems(
            @PathVariable UUID orderId) {
        log.info("Request to get all order items for order ID: {}", orderId);
        List<OrderItemResponse> responses = orderItemService.getOrderItemsByOrderId(orderId);
        return ApiResult.success("Order items retrieved successfully", responses);
    }

    /**
     * Updates an existing order item.
     *
     * @param orderId UUID of the order
     * @param itemId UUID of the order item to update
     * @param request DTO containing order item update details
     * @return ApiResult containing the updated OrderItemResponse or an error if not found
     */
    @PutMapping("/{itemId}")
    @Operation(summary = "Update an order item",
            description = "Updates an existing order item within a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order item updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order item not found or does not belong to the order")
    })
    public ApiResult<OrderItemResponse> updateOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId,
            @Valid @RequestBody OrderItemUpdateRequest request) {
        log.info("Request to update order item with ID: {} for order ID: {}", itemId, orderId);

        // First get the item to verify it belongs to the order
        OrderItemResponse existingItem = orderItemService.getOrderItemById(itemId);
        if (!existingItem.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ApiResult.error("Order item not found in the specified order", null);
        }

        OrderItemResponse response = orderItemService.updateOrderItem(itemId, request);
        return ApiResult.success("Order item updated successfully", response);
    }

    /**
     * Deletes an order item by its ID.
     *
     * @param orderId UUID of the order
     * @param itemId UUID of the order item to delete
     * @return ApiResult with no data content
     */
    @DeleteMapping("/{itemId}")
    @Operation(summary = "Delete an order item",
            description = "Deletes an order item from a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found or does not belong to the order")
    })
    public ApiResult<Void> deleteOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId) {
        log.info("Request to delete order item with ID: {} from order ID: {}", itemId, orderId);

        // First get the item to verify it belongs to the order
        OrderItemResponse existingItem = orderItemService.getOrderItemById(itemId);
        if (!existingItem.getOrderId().equals(orderId)) {
            log.warn("Order item with ID: {} does not belong to order ID: {}", itemId, orderId);
            return ApiResult.error("Order item not found in the specified order", null);
        }

        orderItemService.deleteOrderItem(itemId);
        return ApiResult.success("Order item deleted successfully", null);
    }

    /**
     * Deletes all order items for a specific order.
     *
     * @param orderId UUID of the order
     * @return ApiResult with no data content
     */
    @DeleteMapping
    @Operation(summary = "Delete all order items",
            description = "Deletes all order items for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All order items deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ApiResult<Void> deleteAllOrderItems(
            @PathVariable UUID orderId) {
        log.info("Request to delete all order items for order ID: {}", orderId);
        orderItemService.deleteOrderItemsByOrderId(orderId);
        return ApiResult.success("All order items deleted successfully", null);
    }
}