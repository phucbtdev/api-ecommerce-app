package com.ecommerce_app.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingUpdateRequest {
    @Size(max = 100, message = "Shipping method cannot exceed 100 characters")
    String shippingMethod;

    @DecimalMin(value = "0.0", inclusive = true, message = "Shipping cost must be at least 0.0")
    @Digits(integer = 8, fraction = 2, message = "Shipping cost must have at most 8 integer digits and 2 fraction digits")
    BigDecimal shippingCost;

    @Size(max = 100, message = "Tracking number cannot exceed 100 characters")
    String trackingNumber;

    @Size(max = 100, message = "Carrier cannot exceed 100 characters")
    String carrier;

    @FutureOrPresent(message = "Estimated delivery date must be in the present or future")
    LocalDateTime estimatedDeliveryDate;

    LocalDateTime actualDeliveryDate;

    @Size(max = 50, message = "Shipping status cannot exceed 50 characters")
    String shippingStatus;
}
