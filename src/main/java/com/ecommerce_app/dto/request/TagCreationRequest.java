package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagCreationRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name;

    @Size(max = 255, message = "Slug must not exceed 255 characters")
    String slug;
}
