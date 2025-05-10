package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon extends BaseEntity {

    @Column(nullable = false, unique = true)
    String code;

    @Column(nullable = false)
    String discountType; // PERCENTAGE, FIXED_AMOUNT

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal discountValue;

    @Column(precision = 10, scale = 2)
    BigDecimal minimumPurchaseAmount;

    @Column(precision = 10, scale = 2)
    BigDecimal maximumDiscountAmount;

    LocalDateTime validFrom;

    LocalDateTime validUntil;

    Integer usageLimit;

    Integer usedCount = 0;

    @Column(nullable = false)
    Boolean active = true;

    @ManyToMany
    @JoinTable(
            name = "coupon_categories",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> applicableCategories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "coupon_products",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    Set<Product> applicableProducts = new HashSet<>();

    @OneToMany(mappedBy = "coupon")
    Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "appliedCoupon")
    Set<Cart> carts = new HashSet<>();
}
