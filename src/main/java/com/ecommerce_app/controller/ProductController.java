package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.service.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreationRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    @GetMapping
    public ResponseEntity<Page<ProductBasicResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<ProductBasicResponse>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        return ResponseEntity.ok(productService.getActiveProducts(pageable));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductBasicResponse>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId, pageable));
    }

    @GetMapping("/tag/{tagId}")
    public ResponseEntity<Page<ProductBasicResponse>> getProductsByTag(
            @PathVariable UUID tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        return ResponseEntity.ok(productService.getProductsByTag(tagId, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductBasicResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        return ResponseEntity.ok(productService.searchProducts(keyword, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<Boolean> toggleProductActive(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.toggleProductActive(id));
    }
}