package com.ecommerce_app.dto.response;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusResponse {
    UUID id;
    String name;
    String description;
    String color;
    Integer displayOrder;
    LocalDate createdAt;
    LocalDate updatedAt;
}