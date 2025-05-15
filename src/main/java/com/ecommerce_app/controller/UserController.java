package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.UserCreationRequest;
import com.ecommerce_app.dto.request.UserUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.PageResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.service.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

/**
 * REST controller for managing user operations.
 * This controller provides endpoints for creating, reading, updating, and deleting users.
 * Access to these endpoints is controlled by role-based security.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param userCreationDto Data transfer object containing user creation information
     * @return ApiResult containing the created user response
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user", description = "Creates a new user with the provided details. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> createUser(@Valid @RequestBody UserCreationRequest userCreationDto) {
        UserResponse createdUser = userService.createUser(userCreationDto);
        return ApiResult.success("User created successfully!", createdUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The UUID of the user to retrieve
     * @return ApiResult containing the user response
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Operation(summary = "Get user by ID", description = "Retrieves user information by ID. User can access their own profile, or ADMIN can access any profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> getUserById(@Parameter(description = "User ID") @PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        return ApiResult.success("User retrieved successfully!", user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve
     * @return ApiResult containing the user response
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUsername(#username)")
    @Operation(summary = "Get user by username", description = "Retrieves user information by username. User can access their own profile, or ADMIN can access any profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> getUserByUsername(@Parameter(description = "Username") @PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ApiResult.success("User retrieved successfully!", user);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email of the user to retrieve
     * @return ApiResult containing the user response
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserEmail(#email)")
    @Operation(summary = "Get user by email", description = "Retrieves user information by email. User can access their own profile, or ADMIN can access any profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> getUserByEmail(@Parameter(description = "Email address") @PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);
        return ApiResult.success("User retrieved successfully!", user);
    }

    /**
     * Retrieves a paginated list of all users.
     *
     * @param page Page number (zero-based)
     * @param size Number of items per page
     * @param sortBy Field to sort by
     * @param sortDir Sort direction (asc/desc)
     * @return ApiResult containing the paginated user responses
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieves a paginated list of all users. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<PageResponse<UserResponse>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        PageResponse<UserResponse> users = userService.getAllUsers(page, size, sortBy, sortDir);
        return ApiResult.success("Users retrieved successfully!", users);
    }

    /**
     * Updates a user's information.
     *
     * @param id The UUID of the user to update
     * @param userUpdateDto Data transfer object containing user update information
     * @return ApiResult containing the updated user response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#id)")
    @Operation(summary = "Update user", description = "Updates user information. User can update their own profile, or ADMIN can update any profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> updateUser(
            @Parameter(description = "User ID") @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest userUpdateDto) {
        UserResponse updatedUser = userService.updateUser(id, userUpdateDto);
        return ApiResult.success("User updated successfully!", updatedUser);
    }

    /**
     * Updates a user's roles.
     *
     * @param id The UUID of the user whose roles are to be updated
     * @param roleIds Set of role IDs to assign to the user
     * @return ApiResult containing the updated user response
     */
    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user roles", description = "Updates the roles assigned to a user. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User roles updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> updateUserRoles(
            @Parameter(description = "User ID") @PathVariable UUID id,
            @RequestBody Set<UUID> roleIds) {
        UserResponse updatedUser = userService.updateUserRoles(id, roleIds);
        return ApiResult.success("User roles updated successfully!", updatedUser);
    }

    /**
     * Activates a user account.
     *
     * @param id The UUID of the user to activate
     * @return ApiResult containing the activated user response
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate user", description = "Activates a user account. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> activateUser(@Parameter(description = "User ID") @PathVariable UUID id) {
        UserResponse activatedUser = userService.activateUser(id);
        return ApiResult.success("User activated successfully!", activatedUser);
    }

    /**
     * Deactivates a user account.
     *
     * @param id The UUID of the user to deactivate
     * @return ApiResult containing the deactivated user response
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivates a user account. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<UserResponse> deactivateUser(@Parameter(description = "User ID") @PathVariable UUID id) {
        UserResponse deactivatedUser = userService.deactivateUser(id);
        return ApiResult.success("User deactivated successfully!", deactivatedUser);
    }

    /**
     * Deletes a user.
     *
     * @param id The UUID of the user to delete
     * @return ApiResult with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a user. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<Void> deleteUser(@Parameter(description = "User ID") @PathVariable UUID id) {
        userService.deleteUser(id);
        return ApiResult.success("User deleted successfully!", null);
    }

    /**
     * Checks if a username already exists.
     *
     * @param username The username to check
     * @return ApiResult containing a boolean indicating if the username exists
     */
    @GetMapping("/check/username/{username}")
    @Operation(summary = "Check username availability", description = "Checks if a username is already taken.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<Boolean> checkUsernameExists(@Parameter(description = "Username to check") @PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ApiResult.success("Username availability checked!", exists);
    }

    /**
     * Checks if an email already exists.
     *
     * @param email The email to check
     * @return ApiResult containing a boolean indicating if the email exists
     */
    @GetMapping("/check/email/{email}")
    @Operation(summary = "Check email availability", description = "Checks if an email is already registered.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<Boolean> checkEmailExists(@Parameter(description = "Email to check") @PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ApiResult.success("Email availability checked!", exists);
    }
}