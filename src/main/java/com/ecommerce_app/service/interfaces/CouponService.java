package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CouponCreationRequest;
import com.ecommerce_app.dto.request.CouponUpdateRequest;
import com.ecommerce_app.dto.response.CouponResponse;

import java.util.List;
import java.util.UUID;

public interface CouponService {

    /**
     * Create a new coupon
     *
     * @param request The coupon creation request
     * @return The created coupon response
     */
    CouponResponse createCoupon(CouponCreationRequest request);

    /**
     * Get a coupon by its ID
     *
     * @param id The coupon ID
     * @return The coupon response
     */
    CouponResponse getCouponById(UUID id);

    /**
     * Get a coupon by its code
     *
     * @param code The coupon code
     * @return The coupon response
     */
    CouponResponse getCouponByCode(String code);

    /**
     * Update an existing coupon
     *
     * @param id The coupon ID
     * @param request The coupon update request
     * @return The updated coupon response
     */
    CouponResponse updateCoupon(UUID id, CouponUpdateRequest request);

    /**
     * Delete a coupon by its ID
     *
     * @param id The coupon ID
     */
    void deleteCoupon(UUID id);

    /**
     * Get all coupons
     *
     * @return List of all coupons
     */
    List<CouponResponse> getAllCoupons();

    /**
     * Get all active coupons
     *
     * @return List of active coupons
     */
    List<CouponResponse> getActiveCoupons();

    /**
     * Get all valid coupons (active and within valid date range)
     *
     * @return List of valid coupons
     */
    List<CouponResponse> getValidCoupons();

    /**
     * Get all expired coupons
     *
     * @return List of expired coupons
     */
    List<CouponResponse> getExpiredCoupons();

    /**
     * Get all available coupons (active, within valid date range, and usage limit not reached)
     *
     * @return List of available coupons
     */
    List<CouponResponse> getAvailableCoupons();

    /**
     * Deactivate a coupon
     *
     * @param id The coupon ID
     * @return The updated coupon response
     */
    CouponResponse deactivateCoupon(UUID id);

    /**
     * Activate a coupon
     *
     * @param id The coupon ID
     * @return The updated coupon response
     */
    CouponResponse activateCoupon(UUID id);

    /**
     * Increment the used count of a coupon
     *
     * @param id The coupon ID
     * @return The updated coupon response
     */
    CouponResponse incrementUsedCount(UUID id);
}