package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantAttributeCreationRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Value is mandatory")
    private String value;

    @NotNull(message = "Variant ID is mandatory")
    private java.util.UUID variantId;
}