package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryResponse {
    private UUID id;
    private UUID productVariantId;
    private String productVariantName;
    private Integer stockQuantity;
    private Integer reservedQuantity;
    private Integer reorderLevel;
    private String sku;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer availableQuantity;
}
