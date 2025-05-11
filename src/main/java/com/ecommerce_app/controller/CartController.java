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

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> createCart(@Valid @RequestBody CartCreationRequest cartRequest) {
        CartResponse createdCart = cartService.createCart(cartRequest);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable UUID userId) {
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartUpdateRequest cartUpdateRequest) {
        CartResponse updatedCart = cartService.updateCart(cartId, cartUpdateRequest);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable UUID cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{cartId}/coupons/{couponId}")
    public ResponseEntity<CartResponse> applyCoupon(
            @PathVariable UUID cartId,
            @PathVariable UUID couponId) {
        CartResponse updatedCart = cartService.applyCoupon(cartId, couponId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/coupons")
    public ResponseEntity<CartResponse> removeCoupon(@PathVariable UUID cartId) {
        CartResponse updatedCart = cartService.removeCoupon(cartId);
        return ResponseEntity.ok(updatedCart);
    }

    @PostMapping("/{cartId}/calculate")
    public ResponseEntity<CartResponse> calculateCartTotal(@PathVariable UUID cartId) {
        CartResponse updatedCart = cartService.calculateCartTotal(cartId);
        return ResponseEntity.ok(updatedCart);
    }
}
