package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CouponCreationRequest;
import com.ecommerce_app.dto.request.CouponUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.CouponResponse;
import com.ecommerce_app.service.interfaces.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * REST controller for managing discount coupons.
 * This controller provides endpoints for creating, reading, updating, and deleting coupons,
 * as well as managing their activation status and usage tracking.
 */
@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon Management", description = "APIs for managing discount coupons")
public class CouponController {

    private final CouponService couponService;

    /**
     * Creates a new coupon.
     *
     * @param request Data transfer object containing coupon creation information
     * @return ApiResult containing the created coupon response
     */
    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a new discount coupon with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ApiResult<CouponResponse> createCoupon(@Valid @RequestBody CouponCreationRequest request) {
        CouponResponse createdCoupon = couponService.createCoupon(request);
        return ApiResult.success("Coupon created successfully!", createdCoupon);
    }

    /**
     * Retrieves a coupon by its ID.
     *
     * @param id The UUID of the coupon to retrieve
     * @return ApiResult containing the coupon response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID", description = "Retrieves coupon information by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> getCouponById(@Parameter(description = "Coupon ID") @PathVariable UUID id) {
        CouponResponse coupon = couponService.getCouponById(id);
        return ApiResult.success("Coupon retrieved successfully!", coupon);
    }

    /**
     * Retrieves a coupon by its code.
     *
     * @param code The code of the coupon to retrieve
     * @return ApiResult containing the coupon response
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get coupon by code", description = "Retrieves coupon information by its unique code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> getCouponByCode(@Parameter(description = "Coupon code") @PathVariable String code) {
        CouponResponse coupon = couponService.getCouponByCode(code);
        return ApiResult.success("Coupon retrieved successfully!", coupon);
    }

    /**
     * Updates an existing coupon.
     *
     * @param id The UUID of the coupon to update
     * @param request Data transfer object containing coupon update information
     * @return ApiResult containing the updated coupon response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a coupon", description = "Updates an existing coupon with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> updateCoupon(
            @Parameter(description = "Coupon ID") @PathVariable UUID id,
            @Valid @RequestBody CouponUpdateRequest request) {
        CouponResponse updatedCoupon = couponService.updateCoupon(id, request);
        return ApiResult.success("Coupon updated successfully!", updatedCoupon);
    }

    /**
     * Deletes a coupon.
     *
     * @param id The UUID of the coupon to delete
     * @return ApiResult with no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coupon", description = "Deletes a coupon by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<Void> deleteCoupon(@Parameter(description = "Coupon ID") @PathVariable UUID id) {
        couponService.deleteCoupon(id);
        return ApiResult.success("Coupon deleted successfully!", null);
    }

    /**
     * Retrieves all coupons.
     *
     * @return ApiResult containing the list of all coupon responses
     */
    @GetMapping
    @Operation(summary = "Get all coupons", description = "Retrieves a list of all coupons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CouponResponse>> getAllCoupons() {
        List<CouponResponse> coupons = couponService.getAllCoupons();
        return ApiResult.success("All coupons retrieved successfully!", coupons);
    }

    /**
     * Retrieves all active coupons.
     *
     * @return ApiResult containing the list of active coupon responses
     */
    @GetMapping("/active")
    @Operation(summary = "Get active coupons", description = "Retrieves a list of all active coupons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active coupons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CouponResponse>> getActiveCoupons() {
        List<CouponResponse> activeCoupons = couponService.getActiveCoupons();
        return ApiResult.success("Active coupons retrieved successfully!", activeCoupons);
    }

    /**
     * Retrieves all valid coupons (active and not expired).
     *
     * @return ApiResult containing the list of valid coupon responses
     */
    @GetMapping("/valid")
    @Operation(summary = "Get valid coupons", description = "Retrieves a list of all valid coupons (active and not expired).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid coupons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CouponResponse>> getValidCoupons() {
        List<CouponResponse> validCoupons = couponService.getValidCoupons();
        return ApiResult.success("Valid coupons retrieved successfully!", validCoupons);
    }

    /**
     * Retrieves all expired coupons.
     *
     * @return ApiResult containing the list of expired coupon responses
     */
    @GetMapping("/expired")
    @Operation(summary = "Get expired coupons", description = "Retrieves a list of all expired coupons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expired coupons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CouponResponse>> getExpiredCoupons() {
        List<CouponResponse> expiredCoupons = couponService.getExpiredCoupons();
        return ApiResult.success("Expired coupons retrieved successfully!", expiredCoupons);
    }

    /**
     * Retrieves all available coupons (active, not expired, and not fully used).
     *
     * @return ApiResult containing the list of available coupon responses
     */
    @GetMapping("/available")
    @Operation(summary = "Get available coupons", description = "Retrieves a list of all available coupons (active, not expired, and not fully used).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available coupons retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CouponResponse>> getAvailableCoupons() {
        List<CouponResponse> availableCoupons = couponService.getAvailableCoupons();
        return ApiResult.success("Available coupons retrieved successfully!", availableCoupons);
    }

    /**
     * Deactivates a coupon.
     *
     * @param id The UUID of the coupon to deactivate
     * @return ApiResult containing the deactivated coupon response
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a coupon", description = "Deactivates a coupon by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon deactivated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> deactivateCoupon(@Parameter(description = "Coupon ID") @PathVariable UUID id) {
        CouponResponse deactivatedCoupon = couponService.deactivateCoupon(id);
        return ApiResult.success("Coupon deactivated successfully!", deactivatedCoupon);
    }

    /**
     * Activates a coupon.
     *
     * @param id The UUID of the coupon to activate
     * @return ApiResult containing the activated coupon response
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a coupon", description = "Activates a coupon by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon activated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> activateCoupon(@Parameter(description = "Coupon ID") @PathVariable UUID id) {
        CouponResponse activatedCoupon = couponService.activateCoupon(id);
        return ApiResult.success("Coupon activated successfully!", activatedCoupon);
    }

    /**
     * Increments the usage count of a coupon.
     *
     * @param id The UUID of the coupon to increment usage
     * @return ApiResult containing the updated coupon response
     */
    @PatchMapping("/{id}/increment-usage")
    @Operation(summary = "Increment coupon usage count", description = "Increments the usage count of a coupon by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon usage count incremented successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ApiResult<CouponResponse> incrementUsedCount(@Parameter(description = "Coupon ID") @PathVariable UUID id) {
        CouponResponse updatedCoupon = couponService.incrementUsedCount(id);
        return ApiResult.success("Coupon usage count incremented successfully!", updatedCoupon);
    }
}