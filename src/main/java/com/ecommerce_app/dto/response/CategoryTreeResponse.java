package com.ecommerce_app.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTreeResponse {
    private UUID id;

    private String name;

    private String slug;

    private String imageUrl;

    @Builder.Default
    private Set<CategoryTreeResponse> subcategories = new HashSet<>();
}
