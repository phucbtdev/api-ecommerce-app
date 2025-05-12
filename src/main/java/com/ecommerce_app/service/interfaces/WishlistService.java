package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.WishlistCreationRequest;
import com.ecommerce_app.dto.request.WishlistUpdateRequest;
import com.ecommerce_app.dto.response.WishlistResponse;

import java.util.UUID;

/**
 * Service interface for Wishlist operations.
 */
public interface WishlistService {

    /**
     * Creates a new Wishlist for a user.
     *
     * @param request the creation request
     * @param userId the ID of the user
     * @return the created Wishlist response
     */
    WishlistResponse createWishlist(WishlistCreationRequest request, UUID userId);

    /**
     * Retrieves a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     * @return the Wishlist response
     */
    WishlistResponse getWishlistById(UUID id);

    /**
     * Retrieves a Wishlist by user ID.
     *
     * @param userId the ID of the user
     * @return the Wishlist response
     */
    WishlistResponse getWishlistByUserId(UUID userId);

    /**
     * Updates an existing Wishlist.
     *
     * @param id the ID of the Wishlist
     * @param request the update request
     * @return the updated Wishlist response
     */
    WishlistResponse updateWishlist(UUID id, WishlistUpdateRequest request);

    /**
     * Deletes a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     */
    void deleteWishlist(UUID id);

}
