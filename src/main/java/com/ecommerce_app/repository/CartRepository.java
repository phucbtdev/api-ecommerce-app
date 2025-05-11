package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Cart;
import com.ecommerce_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
}