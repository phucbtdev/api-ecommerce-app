package com.ecommerce_app.dto.response;

import com.ecommerce_app.dto.request.UserProfileCreationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private boolean active;
    private UserProfileCreationRequest profile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
