package com.ecommerce_app.controller;


import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.PermissionResponseDto;
import com.ecommerce_app.service.interfaces.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDto> createPermission(@Valid @RequestBody PermissionCreationRequestDto permissionCreationRequestDto) {
        PermissionResponseDto createdPermission = permissionService.createPermission(permissionCreationRequestDto);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDto> getPermissionById(@PathVariable Long id) {
        PermissionResponseDto permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDto> getPermissionByName(@PathVariable String name) {
        PermissionResponseDto permission = permissionService.getPermissionByName(name);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponseDto>> getAllPermissions() {
        List<PermissionResponseDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponseDto> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequestDto permissionUpdateRequestDto) {
        PermissionResponseDto updatedPermission = permissionService.updatePermission(id, permissionUpdateRequestDto);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> checkPermissionExists(@PathVariable String name) {
        boolean exists = permissionService.existsByName(name);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/names")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponseDto>> getPermissionsByNames(@RequestBody Set<String> names) {
        List<PermissionResponseDto> permissions = permissionService.getPermissionsByNames(names);
        return ResponseEntity.ok(permissions);
    }
}
