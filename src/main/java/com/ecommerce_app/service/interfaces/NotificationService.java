package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.NotificationCreationRequest;
import com.ecommerce_app.dto.request.NotificationUpdateRequest;
import com.ecommerce_app.dto.response.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse createNotification(NotificationCreationRequest request);
    NotificationResponse updateNotification(UUID id, NotificationUpdateRequest request);
    NotificationResponse getNotification(UUID id);
    List<NotificationResponse> getNotificationsByUser(UUID userId);
    void deleteNotification(UUID id);
}
