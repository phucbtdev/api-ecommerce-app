package com.ecommerce_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCreationRequestDto {
    @NotBlank(message = "Permission name is required")
    @Size(min = 3, max = 50, message = "Permission name must be between 3 and 50 characters")
    private String name;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
}
