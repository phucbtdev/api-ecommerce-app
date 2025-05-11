package com.ecommerce_app.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeResponse {
    private Long id;

    private String name;

    private String slug;

    private String imageUrl;

    private Set<CategoryTreeResponse> subcategories = new HashSet<>();
}
