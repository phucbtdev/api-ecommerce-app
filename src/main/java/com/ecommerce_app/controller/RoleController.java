package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.RoleCreationRequest;
import com.ecommerce_app.dto.request.RoleUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.BasicRoleResponse;
import com.ecommerce_app.dto.response.RoleResponse;
import com.ecommerce_app.service.interfaces.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * REST controller for managing user roles.
 * Provides endpoints for CRUD operations on roles and permission management.
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role management APIs")
public class RoleController {

    private final RoleService roleService;

    /**
     * Creates a new role.
     *
     * @param request The role creation request
     * @return API result containing the created role
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Create a new role", description = "Creates a new role with specified permissions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<RoleResponse> createRole(@Valid @RequestBody RoleCreationRequest request) {
        RoleResponse response = roleService.createRole(request);
        return ApiResult.success("Role created successfully", response);
    }

    /**
     * Updates an existing role.
     *
     * @param id The ID of the role
     * @param request The role update request
     * @return API result containing the updated role
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Update a role", description = "Updates an existing role with new information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ApiResult<RoleResponse> updateRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleUpdateRequest request) {
        RoleResponse response = roleService.updateRole(id, request);
        return ApiResult.success("Role updated successfully", response);
    }

    /**
     * Retrieves a role by ID.
     *
     * @param id The ID of the role
     * @return API result containing the role
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Get role by ID", description = "Retrieves a specific role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ApiResult<RoleResponse> getRoleById(@PathVariable UUID id) {
        RoleResponse response = roleService.getRoleById(id);
        return ApiResult.success("Role retrieved successfully", response);
    }

    /**
     * Retrieves a role by name.
     *
     * @param name The name of the role
     * @return API result containing the role
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Get role by name", description = "Retrieves a specific role by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ApiResult<RoleResponse> getRoleByName(@PathVariable String name) {
        RoleResponse response = roleService.getRoleByName(name);
        return ApiResult.success("Role retrieved successfully", response);
    }

    /**
     * Retrieves all roles with pagination.
     *
     * @param pageable Pagination information
     * @return API result containing paged list of roles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Get all roles", description = "Retrieves all roles with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<Page<RoleResponse>> getAllRoles(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<RoleResponse> roles = roleService.getAllRoles(pageable);
        return ApiResult.success("Roles retrieved successfully", roles);
    }

    /**
     * Retrieves basic information of all roles.
     *
     * @return API result containing list of basic role information
     */
    @GetMapping("/basic")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Get all roles (basic info)", description = "Retrieves basic information of all roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Basic role information retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<List<BasicRoleResponse>> getAllRolesBasic() {
        List<BasicRoleResponse> roles = roleService.getAllRolesBasic();
        return ApiResult.success("Basic role information retrieved successfully", roles);
    }

    /**
     * Deletes a role by ID.
     *
     * @param id The ID of the role
     * @return API result with no data content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Delete a role", description = "Deletes a specific role by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ApiResult<Void> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ApiResult.success("Role deleted successfully", null);
    }

    /**
     * Adds permissions to a role.
     *
     * @param id The ID of the role
     * @param permissionIds The set of permission IDs to add
     * @return API result containing the updated role
     */
    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Add permissions to role", description = "Adds specified permissions to an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions added successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role or permissions not found")
    })
    public ApiResult<RoleResponse> addPermissionsToRole(
            @PathVariable UUID id,
            @RequestBody Set<UUID> permissionIds) {
        RoleResponse response = roleService.addPermissionsToRole(id, permissionIds);
        return ApiResult.success("Permissions added to role successfully", response);
    }

    /**
     * Removes permissions from a role.
     *
     * @param id The ID of the role
     * @param permissionIds The set of permission IDs to remove
     * @return API result containing the updated role
     */
    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    @Operation(summary = "Remove permissions from role", description = "Removes specified permissions from an existing role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions removed successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Role or permissions not found")
    })
    public ApiResult<RoleResponse> removePermissionsFromRole(
            @PathVariable UUID id,
            @RequestBody Set<UUID> permissionIds) {
        RoleResponse response = roleService.removePermissionsFromRole(id, permissionIds);
        return ApiResult.success("Permissions removed from role successfully", response);
    }
}