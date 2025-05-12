package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activities")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String activityType;

    @Column(length = 1000)
    String description;

    String ipAddress;

    String userAgent;

    @Column(columnDefinition = "jsonb")
    String metadata;

    LocalDateTime createdAt;
}
