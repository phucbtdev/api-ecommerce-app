package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart  extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> items = new HashSet<>();

    @Column(precision = 10, scale = 2)
    BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    Coupon appliedCoupon;
}
