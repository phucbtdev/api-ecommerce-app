package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.ShippingResponse;
import com.ecommerce_app.service.interfaces.ShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing shipping operations.
 * Provides endpoints for CRUD operations on shipping records and updating shipping status.
 */
@RestController
@RequestMapping("/shippings")
@RequiredArgsConstructor
@Validated
@Tag(name = "Shipping", description = "Shipping management APIs")
public class ShippingController {

    private final ShippingService shippingService;

    /**
     * Creates a new shipping record.
     *
     * @param shippingCreationRequest The shipping creation request
     * @return API result containing the created shipping record
     */
    @PostMapping
    @Operation(summary = "Create shipping", description = "Creates a new shipping record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping record created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<ShippingResponse> createShipping(@RequestBody @Valid ShippingCreationRequest shippingCreationRequest) {
        ShippingResponse response = shippingService.createShipping(shippingCreationRequest);
        return ApiResult.success("Shipping record created successfully", response);
    }

    /**
     * Updates an existing shipping record.
     *
     * @param id The ID of the shipping record
     * @param shippingUpdateRequest The shipping update request
     * @return API result containing the updated shipping record
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update shipping", description = "Updates an existing shipping record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping record updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<ShippingResponse> updateShipping(@PathVariable UUID id,
                                                      @RequestBody @Valid ShippingUpdateRequest shippingUpdateRequest) {
        ShippingResponse response = shippingService.updateShipping(id, shippingUpdateRequest);
        return ApiResult.success("Shipping record updated successfully", response);
    }

    /**
     * Retrieves a shipping record by ID.
     *
     * @param id The ID of the shipping record
     * @return API result containing the shipping record
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get shipping by ID", description = "Retrieves a specific shipping record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping record retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<ShippingResponse> getShippingById(@PathVariable UUID id) {
        ShippingResponse response = shippingService.getShippingById(id);
        return ApiResult.success("Shipping record retrieved successfully", response);
    }

    /**
     * Retrieves a shipping record by order ID.
     *
     * @param orderId The ID of the order
     * @return API result containing the shipping record
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get shipping by order ID", description = "Retrieves shipping record for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping record retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping record or order not found")
    })
    public ApiResult<ShippingResponse> getShippingByOrderId(@PathVariable UUID orderId) {
        ShippingResponse response = shippingService.getShippingByOrderId(orderId);
        return ApiResult.success("Shipping record retrieved successfully", response);
    }

    /**
     * Retrieves all shipping records.
     *
     * @return API result containing list of all shipping records
     */
    @GetMapping
    @Operation(summary = "Get all shippings", description = "Retrieves all shipping records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping records retrieved successfully")
    })
    public ApiResult<List<ShippingResponse>> getAllShippings() {
        List<ShippingResponse> shippings = shippingService.getAllShippings();
        return ApiResult.success("Shipping records retrieved successfully", shippings);
    }

    /**
     * Deletes a shipping record by ID.
     *
     * @param id The ID of the shipping record to delete
     * @return API result with no data content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shipping", description = "Deletes a specific shipping record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<Void> deleteShipping(@PathVariable UUID id) {
        shippingService.deleteShipping(id);
        return ApiResult.success("Shipping record deleted successfully", null);
    }

    /**
     * Updates the status of a shipping record.
     *
     * @param id The ID of the shipping record
     * @param status The new status to set
     * @return API result containing the updated shipping record
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update shipping status", description = "Updates the status of an existing shipping record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipping status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<ShippingResponse> updateShippingStatus(
            @PathVariable UUID id,
            @RequestParam @NotBlank(message = "Status cannot be blank") String status) {
        ShippingResponse response = shippingService.updateShippingStatus(id, status);
        return ApiResult.success("Shipping status updated successfully", response);
    }

    /**
     * Updates the tracking information of a shipping record.
     *
     * @param id The ID of the shipping record
     * @param trackingNumber The tracking number
     * @param carrier The carrier name
     * @return API result containing the updated shipping record
     */
    @PatchMapping("/{id}/tracking")
    @Operation(summary = "Update tracking info", description = "Updates tracking information for a shipping record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracking information updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<ShippingResponse> updateTrackingInfo(
            @PathVariable UUID id,
            @RequestParam @NotBlank(message = "Tracking number cannot be blank") String trackingNumber,
            @RequestParam @NotBlank(message = "Carrier cannot be blank") String carrier) {
        ShippingResponse response = shippingService.updateTrackingInfo(id, trackingNumber, carrier);
        return ApiResult.success("Tracking information updated successfully", response);
    }

    /**
     * Updates the delivery date of a shipping record.
     *
     * @param id The ID of the shipping record
     * @param isActual Whether this is the actual delivery date (true) or estimated (false)
     * @return API result containing the updated shipping record
     */
    @PatchMapping("/{id}/delivery-date")
    @Operation(summary = "Update delivery date", description = "Updates the delivery date for a shipping record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery date updated successfully"),
            @ApiResponse(responseCode = "404", description = "Shipping record not found")
    })
    public ApiResult<ShippingResponse> updateDeliveryDate(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean isActual) {
        ShippingResponse response = shippingService.updateDeliveryDate(id, isActual);
        return ApiResult.success("Delivery date updated successfully", response);
    }

    /**
     * Checks if a shipping record exists for the given order ID.
     *
     * @param orderId The ID of the order
     * @return API result containing boolean indicating if shipping exists
     */
    @GetMapping("/exists/order/{orderId}")
    @Operation(summary = "Check if shipping exists for order",
            description = "Checks if a shipping record exists for the specified order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ApiResult<Boolean> existsByOrderId(@PathVariable UUID orderId) {
        Boolean exists = shippingService.existsByOrderId(orderId);
        return ApiResult.success("Shipping existence check completed", exists);
    }
}