package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerEmail(String customerEmail, Pageable pageable);

    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
