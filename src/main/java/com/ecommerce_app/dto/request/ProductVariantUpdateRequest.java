package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantUpdateRequest {
    @Size(min = 1, max = 255, message = "Variant name must be between 1 and 255 characters")
    private String name;

    private String sku;

    @DecimalMin(value = "-99999.99", message = "Price difference cannot be less than -99999.99")
    @DecimalMax(value = "99999.99", message = "Price difference cannot be more than 99999.99")
    @Digits(integer = 5, fraction = 2, message = "Price difference must have at most 5 digits in whole part and 2 digits in decimal part")
    private BigDecimal priceDifference;
}
