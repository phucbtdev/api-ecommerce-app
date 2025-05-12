package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Wishlist entity.
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    /**
     * Finds a Wishlist by user ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the Wishlist if found
     */
    Optional<Wishlist> findByUserId(UUID userId);
}
