package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserActivityUpdateRequest {
    @Size(max = 255, message = "Activity type must not exceed 255 characters")
    String activityType;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;

    String ipAddress;

    String userAgent;

    String metadata;
}