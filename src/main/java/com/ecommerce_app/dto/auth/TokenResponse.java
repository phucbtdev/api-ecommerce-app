package com.ecommerce_app.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private Set<String> roles;
}
