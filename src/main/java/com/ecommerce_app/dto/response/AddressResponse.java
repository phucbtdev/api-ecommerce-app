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
public class AddressResponse {
    UUID id;
    UUID userId;
    String username;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String postalCode;
    String country;
    String phoneNumber;
    Boolean isDefault;
    String addressType;
    String fullName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
