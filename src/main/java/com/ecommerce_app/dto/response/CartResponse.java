package com.ecommerce_app.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    UUID id;
    UUID userId;
    String userName;
    Set<CartItemResponse> items = new HashSet<>();
    BigDecimal totalAmount;
    UUID couponId;
    String couponCode;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
