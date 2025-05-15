/**
 * Service interface for managing shopping carts in the e-commerce application.
 * Provides functionality for creating, retrieving, updating, and managing carts,
 * including coupon application and total calculation.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CartCreationRequest;
import com.ecommerce_app.dto.request.CartUpdateRequest;
import com.ecommerce_app.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {
    /**
     * Creates a new shopping cart for a user.
     *
     * @param cartRequest The DTO containing cart creation details
     * @return The created cart as a CartResponse
     */
    CartResponse createCart(CartCreationRequest cartRequest);

    /**
     * Retrieves a shopping cart by user ID.
     *
     * @param userId The unique identifier of the user
     * @return The user's cart as a CartResponse
     */
    CartResponse getCartByUserId(UUID userId);

    /**
     * Updates an existing shopping cart.
     *
     * @param cartId The unique identifier of the cart to update
     * @param cartUpdateRequest The DTO containing updated cart details
     * @return The updated cart as a CartResponse
     */
    CartResponse updateCart(UUID cartId, CartUpdateRequest cartUpdateRequest);

    /**
     * Deletes a shopping cart by its unique identifier.
     *
     * @param cartId The unique identifier of the cart to delete
     */
    void deleteCart(UUID cartId);

    /**
     * Applies a coupon to a shopping cart.
     *
     * @param cartId The unique identifier of the cart
     * @param couponId The unique identifier of the coupon to apply
     * @return The updated cart with coupon applied as a CartResponse
     */
    CartResponse applyCoupon(UUID cartId, UUID couponId);

    /**
     * Removes an applied coupon from a shopping cart.
     *
     * @param cartId The unique identifier of the cart
     * @return The updated cart with coupon removed as a CartResponse
     */
    CartResponse removeCoupon(UUID cartId);

    /**
     * Calculates the total price for a shopping cart, including any discounts.
     *
     * @param cartId The unique identifier of the cart
     * @return The cart with calculated totals as a CartResponse
     */
    CartResponse calculateCartTotal(UUID cartId);
}