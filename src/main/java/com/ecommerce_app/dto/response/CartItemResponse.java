package com.ecommerce_app.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    UUID id;
    UUID cartId;
    UUID productId;
    String productName;
    String productImage;
    UUID productVariantId;
    String variantName;
    Integer quantity;
    BigDecimal price;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
