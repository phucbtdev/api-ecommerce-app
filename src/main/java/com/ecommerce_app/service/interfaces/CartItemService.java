/**
 * Service interface for managing individual items within a shopping cart in the e-commerce application.
 * Provides functionality for adding, updating, removing, and retrieving cart items.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CartItemCreationRequest;
import com.ecommerce_app.dto.request.CartItemUpdateRequest;
import com.ecommerce_app.dto.response.CartItemResponse;

import java.util.List;
import java.util.UUID;

public interface CartItemService {
    /**
     * Adds a new item to a specific shopping cart.
     *
     * @param cartId The unique identifier of the cart
     * @param cartItemRequest The DTO containing cart item details
     * @return The added cart item as a CartItemResponse
     */
    CartItemResponse addItemToCart(UUID cartId, CartItemCreationRequest cartItemRequest);

    /**
     * Updates an existing cart item (e.g., change quantity).
     *
     * @param cartItemId The unique identifier of the cart item
     * @param updateRequest The DTO containing updated cart item details
     * @return The updated cart item as a CartItemResponse
     */
    CartItemResponse updateCartItem(UUID cartItemId, CartItemUpdateRequest updateRequest);

    /**
     * Removes a specific item from a shopping cart.
     *
     * @param cartItemId The unique identifier of the cart item to remove
     */
    void removeCartItem(UUID cartItemId);

    /**
     * Retrieves a specific cart item by its unique identifier.
     *
     * @param cartItemId The unique identifier of the cart item
     * @return The cart item as a CartItemResponse
     */
    CartItemResponse getCartItem(UUID cartItemId);

    /**
     * Retrieves all items in a specific shopping cart.
     *
     * @param cartId The unique identifier of the cart
     * @return A list of cart items as CartItemResponse objects
     */
    List<CartItemResponse> getCartItems(UUID cartId);

    /**
     * Removes all items from a specific shopping cart.
     *
     * @param cartId The unique identifier of the cart to clear
     */
    void clearCart(UUID cartId);
}