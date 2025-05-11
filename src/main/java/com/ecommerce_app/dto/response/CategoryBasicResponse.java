package com.ecommerce_app.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBasicResponse {
    private Long id;

    private String name;

    private String slug;

    private String imageUrl;
}