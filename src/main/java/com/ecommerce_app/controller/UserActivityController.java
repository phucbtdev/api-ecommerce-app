package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.UserActivityResponse;
import com.ecommerce_app.service.interfaces.UserActivityService;
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

import java.util.UUID;

/**
 * REST Controller for managing user activities.
 * Provides endpoints for creating, retrieving, updating, and deleting user activities.
 */
@RestController
@RequestMapping("/user-activities")
@RequiredArgsConstructor
@Tag(name = "User Activity", description = "User activity management API")
public class UserActivityController {
    private final UserActivityService userActivityService;

    /**
     * Creates a new user activity.
     *
     * @param request The user activity creation request
     * @return ApiResult containing the created user activity response
     */
    @PostMapping
    @Operation(summary = "Create a new user activity", description = "Creates a new user activity with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User activity successfully created",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<UserActivityResponse> createUserActivity(@Valid @RequestBody UserActivityCreationRequest request) {
        UserActivityResponse response = userActivityService.createUserActivity(request);
        return ApiResult.success("User activity successfully created", response);
    }

    /**
     * Retrieves a user activity by its ID.
     *
     * @param id The ID of the user activity to retrieve
     * @return ApiResult containing the user activity response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user activity by ID", description = "Retrieves a user activity based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activity successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User activity not found")
    })
    public ApiResult<UserActivityResponse> getUserActivity(
            @Parameter(description = "ID of the user activity to retrieve") @PathVariable UUID id) {
        UserActivityResponse response = userActivityService.getUserActivityById(id);
        return ApiResult.success("User activity successfully retrieved", response);
    }

    /**
     * Updates an existing user activity.
     *
     * @param id The ID of the user activity to update
     * @param request The user activity update request
     * @return ApiResult containing the updated user activity response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user activity", description = "Updates a user activity with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activity successfully updated",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User activity not found")
    })
    public ApiResult<UserActivityResponse> updateUserActivity(
            @Parameter(description = "ID of the user activity to update") @PathVariable UUID id,
            @Valid @RequestBody UserActivityUpdateRequest request) {
        UserActivityResponse response = userActivityService.updateUserActivity(id, request);
        return ApiResult.success("User activity successfully updated", response);
    }

    /**
     * Deletes a user activity by its ID.
     *
     * @param id The ID of the user activity to delete
     * @return ApiResult with no data
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user activity", description = "Deletes a user activity with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User activity successfully deleted",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User activity not found")
    })
    public ApiResult<Void> deleteUserActivity(
            @Parameter(description = "ID of the user activity to delete") @PathVariable UUID id) {
        userActivityService.deleteUserActivity(id);
        return ApiResult.success("User activity successfully deleted", null);
    }
}