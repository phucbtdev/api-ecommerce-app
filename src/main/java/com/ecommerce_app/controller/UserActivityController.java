package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.UserActivityResponse;
import com.ecommerce_app.service.interfaces.UserActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/user-activities")
@RequiredArgsConstructor
public class UserActivityController {
    private final UserActivityService userActivityService;

    @PostMapping
    public ResponseEntity<UserActivityResponse> createUserActivity(@Valid @RequestBody UserActivityCreationRequest request) {
        UserActivityResponse response = userActivityService.createUserActivity(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserActivityResponse> getUserActivity(@PathVariable UUID id) {
        UserActivityResponse response = userActivityService.getUserActivityById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserActivityResponse> updateUserActivity(@PathVariable UUID id, @Valid @RequestBody UserActivityUpdateRequest request) {
        UserActivityResponse response = userActivityService.updateUserActivity(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserActivity(@PathVariable UUID id) {
        userActivityService.deleteUserActivity(id);
        return ResponseEntity.noContent().build();
    }
}
