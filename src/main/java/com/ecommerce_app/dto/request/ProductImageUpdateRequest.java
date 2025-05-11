package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageUpdateRequest {
    @Pattern(regexp = "^(https?://|/).+", message = "Image URL must be a valid URL or path")
    private String imageUrl;

    private String alt;

    @Min(value = 0, message = "Sort order cannot be negative")
    private Integer sortOrder;

    private Boolean isMain;
}