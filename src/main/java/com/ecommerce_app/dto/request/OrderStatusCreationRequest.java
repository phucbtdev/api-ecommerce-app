package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusCreationRequest {
    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    String description;

    @Size(max = 7, message = "Color must be a valid hex code (e.g., #FFFFFF)")
    String color;

    Integer displayOrder;
}
