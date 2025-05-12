package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivityResponse {
    UUID id;
    String userId;
    String activityType;
    String description;
    String ipAddress;
    String userAgent;
    String metadata;
    LocalDateTime createdAt;
}