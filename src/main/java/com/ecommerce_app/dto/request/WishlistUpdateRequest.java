package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistUpdateRequest {
    @Size(max = 100, message = "Wishlist name must not exceed 100 characters")
    private String name;

    private Set<UUID> productIds;
}
