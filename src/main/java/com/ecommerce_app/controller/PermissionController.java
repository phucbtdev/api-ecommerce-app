package com.ecommerce_app.controller;


import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.PermissionResponse;
import com.ecommerce_app.service.interfaces.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionCreationRequestDto permissionCreationRequestDto) {
        PermissionResponse createdPermission = permissionService.createPermission(permissionCreationRequestDto);
        return new ResponseEntity<>(createdPermission, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable UUID id) {
        PermissionResponse permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> getPermissionByName(@PathVariable String name) {
        PermissionResponse permission = permissionService.getPermissionByName(name);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable UUID id,
            @Valid @RequestBody PermissionUpdateRequestDto permissionUpdateRequestDto) {
        PermissionResponse updatedPermission = permissionService.updatePermission(id, permissionUpdateRequestDto);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
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
    public ResponseEntity<List<PermissionResponse>> getPermissionsByNames(@RequestBody Set<String> names) {
        List<PermissionResponse> permissions = permissionService.getPermissionsByNames(names);
        return ResponseEntity.ok(permissions);
    }
}
