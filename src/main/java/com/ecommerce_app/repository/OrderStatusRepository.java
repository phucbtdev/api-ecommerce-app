package com.ecommerce_app.repository;

import com.ecommerce_app.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, UUID> {
    Optional<OrderStatus> findByName(String name);
    boolean existsByName(String name);
}