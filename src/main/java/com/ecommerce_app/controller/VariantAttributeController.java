package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.VariantAttributeResponse;
import com.ecommerce_app.service.interfaces.VariantAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/variant-attributes")
@RequiredArgsConstructor
public class VariantAttributeController {

    private final VariantAttributeService service;

    @PostMapping
    public ResponseEntity<VariantAttributeResponse> create(@Valid @RequestBody VariantAttributeCreationRequest request) {
        VariantAttributeResponse response = service.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantAttributeResponse> findById(@PathVariable UUID id) {
        VariantAttributeResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantAttributeResponse> update(@PathVariable UUID id, @Valid @RequestBody VariantAttributeUpdateRequest request) {
        VariantAttributeResponse response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}