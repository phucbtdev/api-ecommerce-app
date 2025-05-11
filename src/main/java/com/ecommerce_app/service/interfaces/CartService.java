package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CartCreationRequest;
import com.ecommerce_app.dto.request.CartUpdateRequest;
import com.ecommerce_app.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse createCart(CartCreationRequest cartRequest);
    CartResponse getCartByUserId(UUID userId);
    CartResponse updateCart(UUID cartId, CartUpdateRequest cartUpdateRequest);
    void deleteCart(UUID cartId);
    CartResponse applyCoupon(UUID cartId, UUID couponId);
    CartResponse removeCoupon(UUID cartId);
    CartResponse calculateCartTotal(UUID cartId);
}