package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.RoleCreationRequest;
import com.ecommerce_app.dto.request.RoleUpdateRequest;
import com.ecommerce_app.dto.response.BasicRoleResponse;
import com.ecommerce_app.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleResponse createRole(RoleCreationRequest request);

    RoleResponse updateRole(Long id, RoleUpdateRequest request);

    RoleResponse getRoleById(Long id);

    RoleResponse getRoleByName(String name);

    Page<RoleResponse> getAllRoles(Pageable pageable);

    List<BasicRoleResponse> getAllRolesBasic();

    void deleteRole(Long id);

    RoleResponse addPermissionsToRole(Long roleId, Set<Long> permissionIds);

    RoleResponse removePermissionsFromRole(Long roleId, Set<Long> permissionIds);
}
