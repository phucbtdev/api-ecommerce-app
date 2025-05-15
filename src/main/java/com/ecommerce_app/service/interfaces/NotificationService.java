/**
 * Service interface for managing user notifications in the e-commerce application.
 * Provides functionality for creating, retrieving, updating, and deleting
 * user notifications for various system events.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.NotificationCreationRequest;
import com.ecommerce_app.dto.request.NotificationUpdateRequest;
import com.ecommerce_app.dto.response.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    /**
     * Creates a new notification.
     *
     * @param request The DTO containing notification creation details
     * @return The created notification as a NotificationResponse
     */
    NotificationResponse createNotification(NotificationCreationRequest request);

    /**
     * Updates an existing notification.
     *
     * @param id The unique identifier of the notification to update
     * @param request The DTO containing updated notification details
     * @return The updated notification as a NotificationResponse
     */
    NotificationResponse updateNotification(UUID id, NotificationUpdateRequest request);

    /**
     * Retrieves a notification by its unique identifier.
     *
     * @param id The unique identifier of the notification
     * @return The notification as a NotificationResponse
     */
    NotificationResponse getNotification(UUID id);

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId The unique identifier of the user
     * @return A list of notifications as NotificationResponse objects
     */
    List<NotificationResponse> getNotificationsByUser(UUID userId);

    /**
     * Deletes a notification by its unique identifier.
     *
     * @param id The unique identifier of the notification to delete
     */
    void deleteNotification(UUID id);
}