package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.OrderStatus;
import com.ecommerce_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserAndCreatedAtBetween(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status.id = :statusId ORDER BY o.createdAt DESC")
    Page<Order> findByUserIdAndStatusId(@Param("userId") UUID userId, @Param("statusId") UUID statusId, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId AND o.status.name = :statusName")
    Long countByUserIdAndStatusName(@Param("userId") UUID userId, @Param("statusName") String statusName);

    boolean existsByOrderNumber(String orderNumber);

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt <= :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.coupon.id = :couponId")
    Long countByCouponId(@Param("couponId") UUID couponId);
}