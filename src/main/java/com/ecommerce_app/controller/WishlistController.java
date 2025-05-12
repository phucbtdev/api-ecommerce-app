package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.WishlistCreationRequest;
import com.ecommerce_app.dto.request.WishlistUpdateRequest;
import com.ecommerce_app.dto.response.WishlistResponse;
import com.ecommerce_app.service.interfaces.WishlistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Wishlist operations.
 */
@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Creates a new Wishlist for a user.
     *
     * @param request the creation request
     * @param userId the ID of the user
     * @return the created Wishlist response
     */
    @PostMapping
    public ResponseEntity<WishlistResponse> createWishlist(
            @Valid @RequestBody WishlistCreationRequest request,
            @RequestParam UUID userId) {
        WishlistResponse response = wishlistService.createWishlist(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     * @return the Wishlist response
     */
    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlistById(@PathVariable UUID id) {
        WishlistResponse response = wishlistService.getWishlistById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a Wishlist by user ID.
     *
     * @param userId the ID of the user
     * @return the Wishlist response
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<WishlistResponse> getWishlistByUserId(@PathVariable UUID userId) {
        WishlistResponse response = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing Wishlist.
     *
     * @param id the ID of the Wishlist
     * @param request the update request
     * @return the updated Wishlist response
     */
    @PutMapping("/{id}")
    public ResponseEntity<WishlistResponse> updateWishlist(
            @PathVariable UUID id,
            @Valid @RequestBody WishlistUpdateRequest request) {
        WishlistResponse response = wishlistService.updateWishlist(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     * @return no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable UUID id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}

