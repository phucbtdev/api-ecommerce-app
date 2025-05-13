package com.ecommerce_app.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicRoleResponse {
    private UUID id;
    private String name;
    private String description;
}
