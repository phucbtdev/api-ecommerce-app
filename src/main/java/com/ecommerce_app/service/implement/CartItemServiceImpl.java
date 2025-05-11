package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.CartItemCreationRequest;
import com.ecommerce_app.dto.request.CartItemUpdateRequest;
import com.ecommerce_app.dto.response.CartItemResponse;
import com.ecommerce_app.entity.Cart;
import com.ecommerce_app.entity.CartItem;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.CartItemMapper;
import com.ecommerce_app.repository.CartItemRepository;
import com.ecommerce_app.repository.CartRepository;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.service.interfaces.CartItemService;
import com.ecommerce_app.service.interfaces.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final CartItemMapper cartItemMapper;
    private final CartService cartService;

    @Override
    @Transactional
    public CartItemResponse addItemToCart(UUID cartId, CartItemCreationRequest cartItemRequest) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        Product product = productRepository.findById(cartItemRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItemRequest.getProductId()));

        ProductVariant productVariant = null;
        if (cartItemRequest.getProductVariantId() != null) {
            productVariant = productVariantRepository.findById(cartItemRequest.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product variant not found with id: " + cartItemRequest.getProductVariantId()));
        }

        // Check if item already exists in cart
        Optional<CartItem> existingCartItem;
        if (productVariant != null) {
            existingCartItem = cartItemRepository.findByCartAndProductAndProductVariant(cart, product, productVariant);
        } else {
            existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);
        }

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            // Update quantity if item already exists
            cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
        } else {
            // Create new cart item
            cartItem = cartItemMapper.toCartItem(cartItemRequest);
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setProductVariant(productVariant);

            // Calculate price based on product and variant
            BigDecimal price = product.getPrice();
            if (productVariant != null && productVariant.getPriceDifference() != null) {
                price = price.add(productVariant.getPriceDifference());
            }
            cartItem.setPrice(price);
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Recalculate cart total
        cartService.calculateCartTotal(cartId);

        return cartItemMapper.toCartItemResponse(savedCartItem);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(UUID cartItemId, CartItemUpdateRequest updateRequest) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        cartItemMapper.updateCartItemFromDto(updateRequest, cartItem);

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        // Recalculate cart total
        cartService.calculateCartTotal(updatedCartItem.getCart().getId());

        return cartItemMapper.toCartItemResponse(updatedCartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(UUID cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        UUID cartId = cartItem.getCart().getId();
        cartItemRepository.delete(cartItem);

        // Recalculate cart total
        cartService.calculateCartTotal(cartId);
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemResponse getCartItem(UUID cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(UUID cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearCart(UUID cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Cart not found with id: " + cartId);
        }

        cartItemRepository.deleteByCartId(cartId);

        // Reset cart total to zero
        cartService.calculateCartTotal(cartId);
    }
}
