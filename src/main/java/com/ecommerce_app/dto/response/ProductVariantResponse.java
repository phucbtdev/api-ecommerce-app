package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {
    private UUID id;
    private String name;
    private String sku;
    private BigDecimal priceDifference;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder.Default
    private Set<VariantAttributeResponse> attributes = new HashSet<>();
    @Builder.Default
    private Set<ProductImageResponse> images = new HashSet<>();
    private InventoryResponse inventory;
}
