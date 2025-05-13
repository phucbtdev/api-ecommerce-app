package com.ecommerce_app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits in whole part and 2 digits in decimal part")
    private BigDecimal price;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    private String slug;

    private String sku;

    private Boolean active;

    @DecimalMin(value = "0.0", message = "Weight cannot be negative")
    private BigDecimal weight;

    private String dimensions;

    @Builder.Default
    private Set<UUID> categoryIds = new HashSet<>();

    @Builder.Default
    private Set<UUID> tagIds = new HashSet<>();

    @Builder.Default
    @Valid
    private Set<ProductVariantCreationRequest> variants = new HashSet<>();

    @Builder.Default
    @Valid
    private Set<ProductImageCreationRequest> images = new HashSet<>();
}
