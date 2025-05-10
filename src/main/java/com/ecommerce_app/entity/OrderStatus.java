package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_statuses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatus extends BaseEntity {

    @Column(nullable = false, unique = true)
    String name;

    String description;

    String color;

    Integer displayOrder;

    @OneToMany(mappedBy = "status")
    Set<Order> orders = new HashSet<>();
}