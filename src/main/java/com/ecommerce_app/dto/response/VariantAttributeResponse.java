package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantAttributeResponse {
    private UUID id;
    private String name;
    private String value;
    private UUID variantId;
}