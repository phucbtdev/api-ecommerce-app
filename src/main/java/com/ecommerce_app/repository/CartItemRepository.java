package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Cart;
import com.ecommerce_app.entity.CartItem;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCartId(UUID cartId);
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProductAndProductVariant(Cart cart, Product product, ProductVariant productVariant);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCartId(UUID cartId);
}