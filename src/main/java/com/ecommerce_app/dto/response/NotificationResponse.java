package com.ecommerce_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private UUID id;
    private String userId;
    private String type;
    private String title;
    private String content;
    private Boolean isRead;
    private String metadata;
    private String createdAt;
    private String updatedAt;
}