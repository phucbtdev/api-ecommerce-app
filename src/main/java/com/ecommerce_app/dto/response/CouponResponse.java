package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponse {
    UUID id;
    String code;
    String discountType;
    BigDecimal discountValue;
    BigDecimal minimumPurchaseAmount;
    BigDecimal maximumDiscountAmount;
    LocalDateTime validFrom;
    LocalDateTime validUntil;
    Integer usageLimit;
    Integer usedCount;
    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<CategoryResponse> applicableCategories;
    Set<ProductResponse> applicableProducts;
}
