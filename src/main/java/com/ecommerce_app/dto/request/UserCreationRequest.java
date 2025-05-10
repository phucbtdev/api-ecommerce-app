package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;

    @Builder.Default
    private boolean active = true;
}
