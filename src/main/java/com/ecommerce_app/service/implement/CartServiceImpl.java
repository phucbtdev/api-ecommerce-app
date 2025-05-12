package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.CartCreationRequest;
import com.ecommerce_app.dto.request.CartUpdateRequest;
import com.ecommerce_app.dto.response.CartResponse;
import com.ecommerce_app.entity.Cart;
import com.ecommerce_app.entity.CartItem;
import com.ecommerce_app.entity.Coupon;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.CartMapper;
import com.ecommerce_app.repository.CartItemRepository;
import com.ecommerce_app.repository.CartRepository;
import com.ecommerce_app.repository.CouponRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponse createCart(CartCreationRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + cartRequest.getUserId()));

        // Check if cart already exists for user
        if (cartRepository.existsByUserId(user.getId())) {
            throw new IllegalStateException("Cart already exists for user with id: " + user.getId());
        }

        Cart cart = cartMapper.toCart(cartRequest);
        cart.setUser(user);
        cart.setTotalAmount(BigDecimal.ZERO);

        // Apply coupon if provided
        if (cartRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(cartRequest.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + cartRequest.getCouponId()));
            cart.setAppliedCoupon(coupon);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
        return cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCart(UUID cartId, CartUpdateRequest cartUpdateRequest) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        cartMapper.updateCartFromDto(cartUpdateRequest, cart);

        // Apply coupon if provided
        if (cartUpdateRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(cartUpdateRequest.getCouponId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + cartUpdateRequest.getCouponId()));
            cart.setAppliedCoupon(coupon);
        } else {
            cart.setAppliedCoupon(null);
        }

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(updatedCart);
    }

    @Override
    @Transactional
    public void deleteCart(UUID cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Cart not found with id: " + cartId);
        }
        cartItemRepository.deleteByCartId(cartId);
        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public CartResponse applyCoupon(UUID cartId, UUID couponId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + couponId));

        cart.setAppliedCoupon(coupon);
        Cart updatedCart = cartRepository.save(cart);

        // Recalculate total
        return calculateCartTotal(cartId);
    }

    @Override
    @Transactional
    public CartResponse removeCoupon(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        cart.setAppliedCoupon(null);
        Cart updatedCart = cartRepository.save(cart);

        // Recalculate total
        return calculateCartTotal(cartId);
    }

    @Override
    @Transactional
    public CartResponse calculateCartTotal(UUID cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + cartId));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }

        // Apply coupon discount if any
        if (cart.getAppliedCoupon() != null) {
            // Implement coupon discount logic here
            // For example:
            // if (cart.getAppliedCoupon().getDiscountType().equals("PERCENTAGE")) {
            //    BigDecimal discountPercentage = cart.getAppliedCoupon().getDiscountValue().divide(new BigDecimal(100));
            //    BigDecimal discount = total.multiply(discountPercentage);
            //    total = total.subtract(discount);
            // } else if (cart.getAppliedCoupon().getDiscountType().equals("FIXED")) {
            //    total = total.subtract(cart.getAppliedCoupon().getDiscountValue());
            // }
        }

        cart.setTotalAmount(total);
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toCartResponse(updatedCart);
    }
}