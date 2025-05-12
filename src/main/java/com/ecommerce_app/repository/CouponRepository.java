package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    List<Coupon> findByActive(Boolean active);

    List<Coupon> findByValidFromBeforeAndValidUntilAfter(LocalDateTime now, LocalDateTime now2);

    List<Coupon> findByValidUntilBefore(LocalDateTime now);

    List<Coupon> findByActiveAndValidFromBeforeAndValidUntilAfter(Boolean active, LocalDateTime now, LocalDateTime now2);

    List<Coupon> findByUsedCountLessThanUsageLimit();
}
