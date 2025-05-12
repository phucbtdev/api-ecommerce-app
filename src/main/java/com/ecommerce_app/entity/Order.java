package com.ecommerce_app.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    @Column(nullable = false, unique = true)
    String orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    BigDecimal shippingAmount;

    @Column(precision = 10, scale = 2)
    BigDecimal taxAmount;

    @Column(precision = 10, scale = 2)
    BigDecimal discountAmount;

    String customerNotes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    Set<OrderItem> orderItems = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    Shipping shipping;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    Address shippingAddress;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    Coupon coupon;

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}