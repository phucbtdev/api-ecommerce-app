package com.ecommerce_app.dto.response;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {
    Long id;
    String name;
    String description;
    String color;
    Integer displayOrder;
    Long createdAt;
    Long updatedAt;
}