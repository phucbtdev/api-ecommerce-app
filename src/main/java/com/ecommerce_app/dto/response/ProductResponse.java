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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String slug;
    private String sku;
    private Boolean active;
    private BigDecimal weight;
    private String dimensions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private Set<CategoryResponse> categories = new HashSet<>();

    @Builder.Default
    private Set<TagResponse> tags = new HashSet<>();

    @Builder.Default
    private Set<ProductImageResponse> images = new HashSet<>();

    @Builder.Default
    private Set<ProductVariantResponse> variants = new HashSet<>();
}
