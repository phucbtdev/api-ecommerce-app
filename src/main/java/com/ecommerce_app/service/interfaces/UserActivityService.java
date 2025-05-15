/**
 * Service interface that manages user activity operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * user activity records. It tracks user interactions and behavior within the system.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.UserActivityResponse;

import java.util.UUID;

public interface UserActivityService {
    /**
     * Creates a new user activity record in the system.
     *
     * @param request The {@link UserActivityCreationRequest} containing activity details
     * @return {@link UserActivityResponse} representing the created user activity
     */
    UserActivityResponse createUserActivity(UserActivityCreationRequest request);

    /**
     * Retrieves a user activity record by its unique identifier.
     *
     * @param id The UUID of the user activity to retrieve
     * @return {@link UserActivityResponse} containing the requested user activity details
     */
    UserActivityResponse getUserActivityById(UUID id);

    /**
     * Updates an existing user activity record.
     *
     * @param id The UUID of the user activity to update
     * @param request The {@link UserActivityUpdateRequest} containing updated activity details
     * @return {@link UserActivityResponse} representing the updated user activity
     */
    UserActivityResponse updateUserActivity(UUID id, UserActivityUpdateRequest request);

    /**
     * Deletes a user activity record from the system.
     *
     * @param id The UUID of the user activity to delete
     */
    void deleteUserActivity(UUID id);
}