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
/**
 * REST controller for managing cart items.
 * Provides endpoints for adding, updating, retrieving, and removing items in a cart.
 */
@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * Adds an item to a cart.
     *
     * @param cartId          the ID of the cart to which the item will be added
     * @param cartItemRequest the request containing details of the item to be added
     * @return the added cart item
     */
    @PostMapping("/cart/{cartId}")
    public ResponseEntity<CartItemResponse> addItemToCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartItemCreationRequest cartItemRequest) {
        CartItemResponse addedItem = cartItemService.addItemToCart(cartId, cartItemRequest);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    /**
     * Updates an existing cart item.
     *
     * @param cartItemId   the ID of the cart item to be updated
     * @param updateRequest the request containing updated details of the cart item
     * @return the updated cart item
     */
    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable UUID cartItemId,
            @Valid @RequestBody CartItemUpdateRequest updateRequest) {
        CartItemResponse updatedItem = cartItemService.updateCartItem(cartItemId, updateRequest);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Removes a cart item.
     *
     * @param cartItemId the ID of the cart item to be removed
     * @return a response with no content
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable UUID cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a cart item by its ID.
     *
     * @param cartItemId the ID of the cart item to retrieve
     * @return the retrieved cart item
     */
    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable UUID cartItemId) {
        CartItemResponse cartItem = cartItemService.getCartItem(cartItemId);
        return ResponseEntity.ok(cartItem);
    }

    /**
     * Retrieves all items in a cart.
     *
     * @param cartId the ID of the cart whose items are to be retrieved
     * @return a list of cart items
     */
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable UUID cartId) {
        List<CartItemResponse> cartItems = cartItemService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * Clears all items from a cart.
     *
     * @param cartId the ID of the cart to clear
     * @return a response with no content
     */
    @DeleteMapping("/cart/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {
        cartItemService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
