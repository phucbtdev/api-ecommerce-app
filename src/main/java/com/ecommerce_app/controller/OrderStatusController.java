
package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.OrderStatusResponse;
import com.ecommerce_app.service.interfaces.OrderStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing order statuses in the e-commerce application.
 * Provides endpoints for creating, retrieving, updating, and deleting order statuses.
 */
@RestController
@RequestMapping("/order-statuses")
@RequiredArgsConstructor
@Tag(name = "Order Status Management", description = "APIs for order status operations")
public class OrderStatusController {
    private final OrderStatusService service;

    /**
     * Creates a new order status.
     *
     * @param request DTO containing order status creation details
     * @return ApiResult containing the created OrderStatusResponse
     */
    @PostMapping
    @Operation(summary = "Create an order status", description = "Creates a new order status with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order status created successfully",
                    content = @Content(schema = @Schema(implementation = OrderStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<OrderStatusResponse> create(@Valid @RequestBody OrderStatusCreationRequest request) {
        return ApiResult.success("Order status created successfully", service.createOrderStatus(request));
    }

    /**
     * Retrieves an order status by its ID.
     *
     * @param id UUID of the order status to retrieve
     * @return ApiResult containing the OrderStatusResponse
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order status by ID", description = "Retrieves an order status using its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status found",
                    content = @Content(schema = @Schema(implementation = OrderStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order status not found")
    })
    public ApiResult<OrderStatusResponse> get(@PathVariable UUID id) {
        return ApiResult.success("Order status retrieved successfully", service.getOrderStatus(id));
    }

    /**
     * Retrieves all order statuses.
     *
     * @return ApiResult containing a list of OrderStatusResponse objects
     */
    @GetMapping
    @Operation(summary = "Get all order statuses", description = "Retrieves all available order statuses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order statuses retrieved successfully")
    })
    public ApiResult<List<OrderStatusResponse>> getAll() {
        return ApiResult.success("All order statuses retrieved successfully", service.getAllOrderStatuses());
    }

    /**
     * Updates an existing order status.
     *
     * @param id UUID of the order status to update
     * @param request DTO containing order status update details
     * @return ApiResult containing the updated OrderStatusResponse
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an order status", description = "Updates an existing order status with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderStatusResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order status not found")
    })
    public ApiResult<OrderStatusResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ApiResult.success("Order status updated successfully", service.updateOrderStatus(id, request));
    }

    /**
     * Deletes an order status by its ID.
     *
     * @param id UUID of the order status to delete
     * @return ApiResult with no data content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order status", description = "Deletes an order status by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order status deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order status not found"),
            @ApiResponse(responseCode = "409", description = "Order status is in use and cannot be deleted")
    })
    public ApiResult<Void> delete(@PathVariable UUID id) {
        service.deleteOrderStatus(id);
        return ApiResult.success("Order status deleted successfully", null);
    }
}