package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponse {
    private UUID id;

    private String name;

    private UUID userId;

    private Set<ProductResponse> products;
}
