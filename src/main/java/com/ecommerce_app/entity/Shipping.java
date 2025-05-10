package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shippings")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shipping extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Column(nullable = false)
    String shippingMethod;

    @Column(precision = 10, scale = 2)
    BigDecimal shippingCost;

    String trackingNumber;

    String carrier;

    LocalDateTime estimatedDeliveryDate;

    LocalDateTime actualDeliveryDate;

    String shippingStatus;
}
