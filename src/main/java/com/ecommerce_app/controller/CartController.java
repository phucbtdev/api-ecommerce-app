package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CartCreationRequest;
import com.ecommerce_app.dto.request.CartUpdateRequest;
import com.ecommerce_app.dto.response.CartResponse;
import com.ecommerce_app.service.interfaces.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing carts.
 * Provides endpoints for creating, updating, retrieving, and deleting carts,
 * as well as applying and removing coupons, and calculating cart totals.
 */
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Creates a new cart.
     *
     * @param cartRequest the request containing details of the cart to be created
     * @return the created cart
     */
    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartCreationRequest cartRequest) {
        CartResponse createdCart = cartService.createCart(cartRequest);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    /**
     * Retrieves a cart by the user ID.
     *
     * @param userId the ID of the user whose cart is to be retrieved
     * @return the retrieved cart
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable UUID userId) {
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Updates an existing cart.
     *
     * @param cartId the ID of the cart to be updated
     * @param cartUpdateRequest the request containing updated details of the cart
     * @return the updated cart
     */
    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartUpdateRequest cartUpdateRequest) {
        CartResponse updatedCart = cartService.updateCart(cartId, cartUpdateRequest);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Deletes a cart by its ID.
     *
     * @param cartId the ID of the cart to delete
     * @return a response with no content
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Applies a coupon to a cart.
     *
     * @param cartId the ID of the cart
     * @param couponId the ID of the coupon to apply
     * @return the updated cart with the applied coupon
     */
    @PostMapping("/{cartId}/coupons/{couponId}")
    public ResponseEntity<CartResponse> applyCoupon(
            @PathVariable UUID cartId,
            @PathVariable UUID couponId) {
        CartResponse updatedCart = cartService.applyCoupon(cartId, couponId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Removes a coupon from a cart.
     *
     * @param cartId the ID of the cart
     * @return the updated cart without the coupon
     */
    @DeleteMapping("/{cartId}/coupons")
    public ResponseEntity<CartResponse> removeCoupon(@PathVariable UUID cartId) {
        CartResponse updatedCart = cartService.removeCoupon(cartId);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Calculates the total for a cart.
     *
     * @param cartId the ID of the cart
     * @return the updated cart with the calculated total
     */
    @PostMapping("/{cartId}/calculate")
    public ResponseEntity<CartResponse> calculateCartTotal(@PathVariable UUID cartId) {
        CartResponse updatedCart = cartService.calculateCartTotal(cartId);
        return ResponseEntity.ok(updatedCart);
    }
}