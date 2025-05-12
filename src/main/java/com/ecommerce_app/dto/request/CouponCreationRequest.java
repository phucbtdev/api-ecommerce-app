package com.ecommerce_app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreationRequest {
    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Code must be between 3 and 50 characters")
    String code;

    @NotBlank(message = "Discount type is required")
    @Pattern(regexp = "^(PERCENTAGE|FIXED_AMOUNT)$", message = "Discount type must be either PERCENTAGE or FIXED_AMOUNT")
    String discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Discount value is too large")
    BigDecimal discountValue;

    @DecimalMin(value = "0.00", message = "Minimum purchase amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Minimum purchase amount is too large")
    BigDecimal minimumPurchaseAmount;

    @DecimalMin(value = "0.00", message = "Maximum discount amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Maximum discount amount is too large")
    BigDecimal maximumDiscountAmount;

    LocalDateTime validFrom;

    LocalDateTime validUntil;

    @Min(value = 0, message = "Usage limit cannot be negative")
    Integer usageLimit;

    Boolean active = true;

    Set<UUID> applicableCategoryIds;

    Set<UUID> applicableProductIds;
}
