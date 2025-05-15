package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.UserCreationRequest;
import com.ecommerce_app.dto.request.UserUpdateRequest;
import com.ecommerce_app.dto.response.PageResponse;
import com.ecommerce_app.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface  UserService {
    /**
     * Create a new user
     *
     * @param userCreationDto user creation data
     * @return created user response
     */
    UserResponse createUser(UserCreationRequest userCreationDto);

    /**
     * Get user by ID
     *
     * @param id user ID
     * @return user response
     */
    UserResponse getUserById(UUID id);

    /**
     * Get user by username
     *
     * @param username username
     * @return user response
     */
    UserResponse getUserByUsername(String username);

    /**
     * Get user by email
     *
     * @param email email address
     * @return user response
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all users
     *
     * @return list of all users
     */
    PageResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDir);

    /**
     * Update user
     *
     * @param id user ID
     * @param userUpdateDto user update data
     * @return updated user response
     */
    UserResponse updateUser(UUID id, UserUpdateRequest userUpdateDto);

    /**
     * Update user roles
     *
     * @param userId user ID
     * @param roleIds set of role IDs
     * @return updated user response
     */
    UserResponse updateUserRoles(UUID userId, Set<UUID> roleIds);

    /**
     * Activate user
     *
     * @param id user ID
     * @return activated user response
     */
    UserResponse activateUser(UUID id);

    /**
     * Deactivate user
     *
     * @param id user ID
     * @return deactivated user response
     */
    UserResponse deactivateUser(UUID id);

    /**
     * Delete user
     *
     * @param id user ID
     */
    void deleteUser(UUID id);

    /**
     * Check if username exists
     *
     * @param username username
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email email address
     * @return true if email exists
     */
    boolean existsByEmail(String email);
}
