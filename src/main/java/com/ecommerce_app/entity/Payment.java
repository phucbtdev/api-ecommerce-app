package com.ecommerce_app.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @Column(nullable = false)
    String paymentMethod;

    @Column(nullable = false)
    String paymentStatus;

    String transactionId;

    @Column(precision = 10, scale = 2)
    BigDecimal amount;

    String currency;

    @Column(length = 2000)
    String paymentDetails;

    LocalDateTime paymentDate;

}