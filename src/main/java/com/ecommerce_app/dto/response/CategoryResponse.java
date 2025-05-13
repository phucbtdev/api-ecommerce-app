package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private UUID id;

    private String name;

    private String description;

    private String slug;

    private String imageUrl;

    private CategoryBasicResponse parent;

    @Builder.Default
    private Set<CategoryBasicResponse> subcategories = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int productCount;
}
