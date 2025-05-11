package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageCreationRequest {
    @NotBlank(message = "Image URL is required")
    @Pattern(regexp = "^(https?://|/).+", message = "Image URL must be a valid URL or path")
    private String imageUrl;

    private String alt;

    @Min(value = 0, message = "Sort order cannot be negative")
    private Integer sortOrder;

    private Boolean isMain;

    // Only needed when adding image to existing variant
    private UUID variantId;
}