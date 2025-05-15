/**
 * Service interface that manages role operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * user roles. It handles permission management for roles and supports role-based
 * access control within the system.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.RoleCreationRequest;
import com.ecommerce_app.dto.request.RoleUpdateRequest;
import com.ecommerce_app.dto.response.BasicRoleResponse;
import com.ecommerce_app.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService {
    /**
     * Creates a new role in the system.
     *
     * @param request The {@link RoleCreationRequest} containing role details
     * @return {@link RoleResponse} representing the created role
     */
    RoleResponse createRole(RoleCreationRequest request);

    /**
     * Updates an existing role.
     *
     * @param id The UUID of the role to update
     * @param request The {@link RoleUpdateRequest} containing updated role details
     * @return {@link RoleResponse} representing the updated role
     */
    RoleResponse updateRole(UUID id, RoleUpdateRequest request);

    /**
     * Retrieves a role by its unique identifier.
     *
     * @param id The UUID of the role to retrieve
     * @return {@link RoleResponse} containing the requested role details
     */
    RoleResponse getRoleById(UUID id);

    /**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to retrieve
     * @return {@link RoleResponse} containing the requested role details
     */
    RoleResponse getRoleByName(String name);

    /**
     * Retrieves all roles with pagination support.
     *
     * @param pageable The pagination information
     * @return A page of {@link RoleResponse} objects representing roles
     */
    Page<RoleResponse> getAllRoles(Pageable pageable);

    /**
     * Retrieves all roles with simplified information.
     *
     * @return A list of {@link BasicRoleResponse} objects representing all roles
     */
    List<BasicRoleResponse> getAllRolesBasic();

    /**
     * Deletes a role from the system.
     *
     * @param id The UUID of the role to delete
     */
    void deleteRole(UUID id);

    /**
     * Adds a set of permissions to an existing role.
     *
     * @param roleId The UUID of the role to add permissions to
     * @param permissionIds A set of UUIDs representing permissions to add
     * @return {@link RoleResponse} representing the updated role
     */
    RoleResponse addPermissionsToRole(UUID roleId, Set<UUID> permissionIds);

    /**
     * Removes a set of permissions from an existing role.
     *
     * @param roleId The UUID of the role to remove permissions from
     * @param permissionIds A set of UUIDs representing permissions to remove
     * @return {@link RoleResponse} representing the updated role
     */
    RoleResponse removePermissionsFromRole(UUID roleId, Set<UUID> permissionIds);
}