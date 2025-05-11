package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CartItemCreationRequest;
import com.ecommerce_app.dto.request.CartItemUpdateRequest;
import com.ecommerce_app.dto.response.CartItemResponse;
import com.ecommerce_app.service.interfaces.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/cart/{cartId}")
    public ResponseEntity<CartItemResponse> addItemToCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartItemCreationRequest cartItemRequest) {
        CartItemResponse addedItem = cartItemService.addItemToCart(cartId, cartItemRequest);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable UUID cartItemId,
            @Valid @RequestBody CartItemUpdateRequest updateRequest) {
        CartItemResponse updatedItem = cartItemService.updateCartItem(cartItemId, updateRequest);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable UUID cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable UUID cartItemId) {
        CartItemResponse cartItem = cartItemService.getCartItem(cartItemId);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable UUID cartId) {
        List<CartItemResponse> cartItems = cartItemService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        cartItemService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
