package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBasicResponse {
    private UUID id;
    private String name;
    private String slug;
    private BigDecimal price;
    private Boolean active;
    private String mainImageUrl;
}