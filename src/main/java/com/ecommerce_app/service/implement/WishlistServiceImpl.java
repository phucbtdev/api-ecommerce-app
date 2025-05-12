package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.WishlistCreationRequest;
import com.ecommerce_app.dto.request.WishlistUpdateRequest;
import com.ecommerce_app.dto.response.WishlistResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.Wishlist;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.WishlistMapper;
import com.ecommerce_app.repository.ProductRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.repository.WishlistRepository;
import com.ecommerce_app.service.interfaces.WishlistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of WishlistService.
 */
@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional
    public WishlistResponse createWishlist(WishlistCreationRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = wishlistMapper.toEntity(request);
        wishlist.setUser(user);

        if (request.getProductIds() != null) {
            Set<Product> products = request.getProductIds().stream()
                    .map(productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId)))
                    .collect(Collectors.toSet());
            wishlist.setProducts(products);
        }

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toResponse(savedWishlist);
    }

    @Override
    public WishlistResponse getWishlistById(UUID id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + id));
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    public WishlistResponse getWishlistByUserId(UUID userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id: " + userId));
        return wishlistMapper.toResponse(wishlist);
    }

    @Override
    @Transactional
    public WishlistResponse updateWishlist(UUID id, WishlistUpdateRequest request) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + id));

        wishlistMapper.updateEntityFromRequest(request, wishlist);

        if (request.getProductIds() != null) {
            Set<Product> products = request.getProductIds().stream()
                    .map(productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId)))
                    .collect(Collectors.toSet());
            wishlist.setProducts(products);
        }

        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        return wishlistMapper.toResponse(updatedWishlist);
    }

    @Override
    @Transactional
    public void deleteWishlist(UUID id) {
        Wishlist wishlist = wishlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found with id: " + id));
        wishlistRepository.delete(wishlist);
    }
}
