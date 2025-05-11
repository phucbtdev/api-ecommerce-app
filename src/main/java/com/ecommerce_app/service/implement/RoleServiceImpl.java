package com.ecommerce_app.service.implement;


import com.ecommerce_app.dto.request.RoleCreationRequest;
import com.ecommerce_app.dto.request.RoleUpdateRequest;
import com.ecommerce_app.dto.response.BasicRoleResponse;
import com.ecommerce_app.dto.response.RoleResponse;
import com.ecommerce_app.entity.Permission;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.RoleMapper;
import com.ecommerce_app.repository.PermissionRepository;
import com.ecommerce_app.repository.RoleRepository;
import com.ecommerce_app.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleCreationRequest request) {
        log.info("Creating new role with name: {}", request.getName());

        if (roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role", "name", request.getName());
        }

        Role role = roleMapper.toEntity(request);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = getPermissionsByIds(request.getPermissionIds());
            role.setPermissions(permissions);
        }

        Role savedRole = roleRepository.save(role);
        log.info("Role created successfully with ID: {}", savedRole.getId());

        return roleMapper.toResponseDto(savedRole);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleUpdateRequest request) {
        log.info("Updating role with ID: {}", id);

        Role role = findRoleById(id);

        if (request.getName() != null && !request.getName().equals(role.getName()) &&
                roleRepository.existsByName(request.getName())) {
            throw new ResourceAlreadyExistsException("Role", "name", request.getName());
        }

        roleMapper.updateEntityFromDto(request, role);

        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = getPermissionsByIds(request.getPermissionIds());
            role.setPermissions(permissions);
        }

        Role updatedRole = roleRepository.save(role);
        log.info("Role updated successfully with ID: {}", updatedRole.getId());

        return roleMapper.toResponseDto(updatedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleById(Long id) {
        log.info("Fetching role with ID: {}", id);
        Role role = findRoleById(id);
        return roleMapper.toResponseDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRoleByName(String name) {
        log.info("Fetching role with name: {}", name);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", name));
        return roleMapper.toResponseDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponse> getAllRoles(Pageable pageable) {
        log.info("Fetching all roles with pagination");
        return roleRepository.findAll(pageable)
                .map(roleMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasicRoleResponse> getAllRolesBasic() {
        log.info("Fetching all roles without pagination (basic info)");
        return roleRepository.findAll().stream()
                .map(roleMapper::toBasicResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRole(Long id) {
        log.info("Deleting role with ID: {}", id);
        Role role = findRoleById(id);

        if (!role.getUsers().isEmpty()) {
            log.warn("Cannot delete role with ID: {} as it is assigned to users", id);
            throw new IllegalStateException("Cannot delete role as it is assigned to users");
        }

        roleRepository.delete(role);
        log.info("Role deleted successfully with ID: {}", id);
    }

    @Override
    public RoleResponse addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        log.info("Adding permissions to role with ID: {}", roleId);

        Role role = findRoleById(roleId);
        Set<Permission> permissionsToAdd = getPermissionsByIds(permissionIds);

        role.getPermissions().addAll(permissionsToAdd);
        Role updatedRole = roleRepository.save(role);

        log.info("Added {} permissions to role with ID: {}", permissionIds.size(), roleId);
        return roleMapper.toResponseDto(updatedRole);
    }

    @Override
    public RoleResponse removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        log.info("Removing permissions from role with ID: {}", roleId);

        Role role = findRoleById(roleId);
        Set<Permission> currentPermissions = new HashSet<>(role.getPermissions());

        currentPermissions.removeIf(permission -> permissionIds.contains(permission.getId()));
        role.setPermissions(currentPermissions);

        Role updatedRole = roleRepository.save(role);
        log.info("Removed {} permissions from role with ID: {}", permissionIds.size(), roleId);

        return roleMapper.toResponseDto(updatedRole);
    }

    private Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id.toString()));
    }

    private Set<Permission> getPermissionsByIds(Set<Long> permissionIds) {
        return permissionIds.stream()
                .map(id -> permissionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission", "id", id.toString())))
                .collect(Collectors.toSet());
    }
}
