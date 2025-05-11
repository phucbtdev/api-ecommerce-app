package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.CartItemCreationRequest;
import com.ecommerce_app.dto.request.CartItemUpdateRequest;
import com.ecommerce_app.dto.response.CartItemResponse;

import java.util.List;
import java.util.UUID;

public interface CartItemService {
    CartItemResponse addItemToCart(UUID cartId, CartItemCreationRequest cartItemRequest);
    CartItemResponse updateCartItem(UUID cartItemId, CartItemUpdateRequest updateRequest);
    void removeCartItem(UUID cartItemId);
    CartItemResponse getCartItem(UUID cartItemId);
    List<CartItemResponse> getCartItems(UUID cartId);
    void clearCart(UUID cartId);
}
