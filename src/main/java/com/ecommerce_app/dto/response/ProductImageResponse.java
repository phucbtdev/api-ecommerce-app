package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {
    private UUID id;
    private String imageUrl;
    private String alt;
    private Integer sortOrder;
    private Boolean isMain;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID variantId;
}