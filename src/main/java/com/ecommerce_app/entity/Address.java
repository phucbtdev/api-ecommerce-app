package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String addressLine1;

    String addressLine2;

    String city;

    String state;

    String postalCode;

    String country;

    String phoneNumber;

    Boolean isDefault = false;

    String addressType; // SHIPPING, BILLING, BOTH

    String fullName;

    @OneToMany(mappedBy = "billingAddress")
    Set<Order> billingOrders = new HashSet<>();

    @OneToMany(mappedBy = "shippingAddress")
    Set<Order> shippingOrders = new HashSet<>();
}
