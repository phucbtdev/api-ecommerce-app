package com.ecommerce_app.dto.request;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemUpdateRequest {
    @Min(value = 1, message = "Quantity must be at least 1")
    Integer quantity;
}
