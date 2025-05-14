package com.ecommerce_app.service.interfaces;



import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.PermissionResponse;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionService {

    /**
     * Create a new permission
     *
     * @param permissionCreationRequestDto permission creation data
     * @return created permission response
     */
    PermissionResponse createPermission(PermissionCreationRequestDto permissionCreationRequestDto);

    /**
     * Get permission by ID
     *
     * @param id permission ID
     * @return permission response
     */
    PermissionResponse getPermissionById(UUID id);

    /**
     * Get permission by name
     *
     * @param name permission name
     * @return permission response
     */
    PermissionResponse getPermissionByName(String name);

    /**
     * Get all permissions
     *
     * @return list of all permissions
     */
    List<PermissionResponse> getAllPermissions();

    /**
     * Update permission
     *
     * @param id permission ID
     * @param permissionUpdateRequestDto permission update data
     * @return updated permission response
     */
    PermissionResponse updatePermission(UUID id, PermissionUpdateRequestDto permissionUpdateRequestDto);

    /**
     * Delete permission
     *
     * @param id permission ID
     */
    void deletePermission(UUID id);

    /**
     * Check if permission exists by name
     *
     * @param name permission name
     * @return true if permission exists
     */
    boolean existsByName(String name);

    /**
     * Get permissions by names
     *
     * @param names set of permission names
     * @return list of permissions with matching names
     */
    List<PermissionResponse> getPermissionsByNames(Set<String> names);
}
