package com.ecommerce_app.dto.response;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;
    Long cartId;
    Long productId;
    String productName;
    String productImage;
    Long productVariantId;
    String variantName;
    Integer quantity;
    BigDecimal price;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
