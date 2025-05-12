package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private UUID id;
    private UUID productId;
    private UUID userId;
    private Integer rating;
    private String comment;
    private String title;
    private Boolean verified;
    private Boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
