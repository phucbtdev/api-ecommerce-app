package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.PermissionResponse;
import com.ecommerce_app.entity.Permission;
import com.ecommerce_app.exception.ResourceAlreadyExistsException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.PermissionMapper;
import com.ecommerce_app.repository.PermissionRepository;
import com.ecommerce_app.service.interfaces.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    @Transactional
    public PermissionResponse createPermission(PermissionCreationRequestDto permissionCreationRequestDto) {
        // Check if permission name already exists
        if (permissionRepository.existsByName(permissionCreationRequestDto.getName())) {
            throw new ResourceAlreadyExistsException("Permission with name " + permissionCreationRequestDto.getName() + " already exists");
        }

        // Create new permission entity
        Permission permission = permissionMapper.toEntity(permissionCreationRequestDto);

        // Save permission
        Permission savedPermission = permissionRepository.save(permission);
        log.info("Created new permission with id: {}", savedPermission.getId());

        return permissionMapper.toResponseDto(savedPermission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission with id " + id + " not found"));

        return permissionMapper.toResponseDto(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public PermissionResponse getPermissionByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Permission with name " + name + " not found"));

        return permissionMapper.toResponseDto(permission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermissionResponse updatePermission(UUID id, PermissionUpdateRequestDto permissionUpdateRequestDto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission with id " + id + " not found"));

        // Check if name is being updated and already exists
        if (permissionUpdateRequestDto.getName() != null &&
                !permission.getName().equals(permissionUpdateRequestDto.getName()) &&
                permissionRepository.existsByName(permissionUpdateRequestDto.getName())) {
            throw new ResourceAlreadyExistsException("Permission with name " + permissionUpdateRequestDto.getName() + " already exists");
        }

        // Update permission entity with values from DTO
        permissionMapper.updateEntityFromDto(permissionUpdateRequestDto, permission);

        // Save updated permission
        Permission updatedPermission = permissionRepository.save(permission);
        log.info("Updated permission with id: {}", updatedPermission.getId());

        return permissionMapper.toResponseDto(updatedPermission);
    }

    @Override
    @Transactional
    public void deletePermission(UUID id) {
        if (!permissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission with id " + id + " not found");
        }

        permissionRepository.deleteById(id);
        log.info("Deleted permission with id: {}", id);
    }

    @Override
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionResponse> getPermissionsByNames(Set<String> names) {
        return permissionRepository.findByNameIn(names).stream()
                .map(permissionMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
