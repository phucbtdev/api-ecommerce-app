package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

    private String slug;

    private String imageUrl;

    private CategoryBasicResponse parent;

    private Set<CategoryBasicResponse> subcategories = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int productCount;
}
