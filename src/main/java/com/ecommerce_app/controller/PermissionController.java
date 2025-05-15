package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.PermissionResponse;
import com.ecommerce_app.service.interfaces.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * REST controller for managing system permissions.
 * Provides endpoints for creating, updating, retrieving, and deleting permissions.
 * Access to these endpoints is restricted to users with admin privileges.
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "API for permission management (admin only)")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Creates a new permission.
     *
     * @param permissionCreationRequestDto The permission details to create
     * @return ApiResult containing the created permission information
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new permission", description = "Creates a new permission with the provided details (admin only)")
    public ApiResult<PermissionResponse> createPermission(@Valid @RequestBody PermissionCreationRequestDto permissionCreationRequestDto) {
        PermissionResponse createdPermission = permissionService.createPermission(permissionCreationRequestDto);
        return ApiResult.success("Permission created successfully", createdPermission);
    }

    /**
     * Retrieves a permission by its ID.
     *
     * @param id The ID of the permission to retrieve
     * @return ApiResult containing the permission information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get permission by ID", description = "Retrieves permission information for the specified ID (admin only)")
    public ApiResult<PermissionResponse> getPermissionById(@PathVariable UUID id) {
        PermissionResponse permission = permissionService.getPermissionById(id);
        return ApiResult.success("Permission retrieved successfully", permission);
    }

    /**
     * Retrieves a permission by its name.
     *
     * @param name The name of the permission to retrieve
     * @return ApiResult containing the permission information
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get permission by name", description = "Retrieves permission information for the specified name (admin only)")
    public ApiResult<PermissionResponse> getPermissionByName(@PathVariable String name) {
        PermissionResponse permission = permissionService.getPermissionByName(name);
        return ApiResult.success("Permission retrieved successfully", permission);
    }

    /**
     * Retrieves all permissions.
     *
     * @return ApiResult containing a list of all permissions
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all permissions", description = "Retrieves a list of all permissions (admin only)")
    public ApiResult<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ApiResult.success("Permissions retrieved successfully", permissions);
    }

    /**
     * Updates an existing permission.
     *
     * @param id The ID of the permission to update
     * @param permissionUpdateRequestDto The permission details to update
     * @return ApiResult containing the updated permission information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing permission", description = "Updates a permission with the provided details (admin only)")
    public ApiResult<PermissionResponse> updatePermission(
            @PathVariable UUID id,
            @Valid @RequestBody PermissionUpdateRequestDto permissionUpdateRequestDto) {
        PermissionResponse updatedPermission = permissionService.updatePermission(id, permissionUpdateRequestDto);
        return ApiResult.success("Permission updated successfully", updatedPermission);
    }

    /**
     * Deletes a permission.
     *
     * @param id The ID of the permission to delete
     * @return ApiResult with a success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a permission", description = "Deletes the permission with the specified ID (admin only)")
    public ApiResult<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ApiResult.success("Permission deleted successfully", null);
    }

    /**
     * Checks if a permission exists by name.
     *
     * @param name The name of the permission to check
     * @return ApiResult containing a boolean indicating if the permission exists
     */
    @GetMapping("/check/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Check if permission exists", description = "Checks if a permission with the specified name exists (admin only)")
    public ApiResult<Boolean> checkPermissionExists(@PathVariable String name) {
        boolean exists = permissionService.existsByName(name);
        return ApiResult.success("Permission existence checked", exists);
    }

    /**
     * Retrieves permissions by their names.
     *
     * @param names The set of permission names to retrieve
     * @return ApiResult containing a list of permissions matching the specified names
     */
    @PostMapping("/names")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get permissions by names", description = "Retrieves permissions for the specified names (admin only)")
    public ApiResult<List<PermissionResponse>> getPermissionsByNames(@RequestBody Set<String> names) {
        List<PermissionResponse> permissions = permissionService.getPermissionsByNames(names);
        return ApiResult.success("Permissions retrieved successfully", permissions);
    }
}