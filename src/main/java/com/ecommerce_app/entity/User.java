package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, unique = true)
    String email;

    String fullName;

    String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    Boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Address> addresses = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Order> orders = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Cart cart;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Review> reviews = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Wishlist wishlist;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Notification> notifications = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<UserActivity> activities = new HashSet<>();
}
