package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantAttributeUpdateRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Value is mandatory")
    private String value;
}