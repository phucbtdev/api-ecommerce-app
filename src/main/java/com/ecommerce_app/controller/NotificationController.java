package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.NotificationCreationRequest;
import com.ecommerce_app.dto.request.NotificationUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.NotificationResponse;
import com.ecommerce_app.service.interfaces.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing user notifications.
 * Provides endpoints for creating, retrieving, updating, and deleting notification records.
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "User notifications management API")
class NotificationController {
    private final NotificationService notificationService;

    /**
     * Creates a new notification.
     *
     * @param request The notification creation request
     * @return The created notification information
     */
    @PostMapping
    @Operation(summary = "Create a new notification",
            description = "Creates a new notification with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<NotificationResponse> createNotification(
            @Parameter(description = "Notification creation details", required = true)
            @Valid @RequestBody NotificationCreationRequest request) {
        return ApiResult.success("Notification created successfully",
                notificationService.createNotification(request));
    }

    /**
     * Updates an existing notification.
     *
     * @param id The UUID of the notification to update
     * @param request The update request containing the new information
     * @return The updated notification information
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update notification", description = "Updates an existing notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<NotificationResponse> updateNotification(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Notification update details", required = true)
            @Valid @RequestBody NotificationUpdateRequest request) {
        return ApiResult.success("Notification updated successfully",
                notificationService.updateNotification(id, request));
    }

    /**
     * Retrieves a notification by its ID.
     *
     * @param id The UUID of the notification to retrieve
     * @return The notification information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID",
            description = "Retrieves notification information for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<NotificationResponse> getNotification(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id) {
        return ApiResult.success("Notification retrieved successfully",
                notificationService.getNotification(id));
    }

    /**
     * Retrieves all notifications for a specific user.
     *
     * @param userId The UUID of the user
     * @return List of notifications for the specified user
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get notifications by user",
            description = "Retrieves all notifications for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<NotificationResponse>> getNotificationsByUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable UUID userId) {
        return ApiResult.success("User notifications retrieved successfully",
                notificationService.getNotificationsByUser(userId));
    }

    /**
     * Deletes a notification.
     *
     * @param id The UUID of the notification to delete
     * @return Empty response with no content status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Deletes an existing notification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notification deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<Void> deleteNotification(
            @Parameter(description = "Notification ID", required = true)
            @PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ApiResult.success("Notification deleted successfully", null);
    }
}