package com.ecommerce_app.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class NotificationUpdateRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;

    private Boolean isRead;

    @Size(max = 1000, message = "Metadata must not exceed 1000 characters")
    private String metadata;
}
