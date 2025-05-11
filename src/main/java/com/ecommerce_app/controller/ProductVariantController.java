package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.ProductVariantResponse;
import com.ecommerce_app.service.interfaces.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/{productId}/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    @Operation(summary = "Create a new product variant")
    public ResponseEntity<ProductVariantResponse> createProductVariant(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductVariantCreationRequest request) {
        ProductVariantResponse variant = productVariantService.createProductVariant(productId, request);
        return ResponseEntity.ok(variant);
    }

    @GetMapping
    @Operation(summary = "Get all variants for a product")
    public ResponseEntity<List<ProductVariantResponse>> getProductVariants(
            @PathVariable UUID productId) {
        List<ProductVariantResponse> variants = productVariantService.getVariantsByProductId(productId);
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/{variantId}")
    @Operation(summary = "Get product variant by ID")
    public ResponseEntity<ProductVariantResponse> getProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId) {
        ProductVariantResponse variant = productVariantService.getProductVariantById(variantId);
        return ResponseEntity.ok(variant);
    }

    @PutMapping("/{variantId}")
    @Operation(summary = "Update a product variant")
    public ResponseEntity<ProductVariantResponse> updateProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId,
            @Valid @RequestBody ProductVariantUpdateRequest request) {
        ProductVariantResponse variant = productVariantService.updateProductVariant(variantId, request);
        return ResponseEntity.ok( variant);
    }

    @DeleteMapping("/{variantId}")
    @Operation(summary = "Delete a product variant")
    public ResponseEntity<Void> deleteProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ResponseEntity.ok( null);
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter product variants by attribute")
    public ResponseEntity<List<ProductVariantResponse>> filterVariantsByAttribute(
            @PathVariable UUID productId,
            @RequestParam String attributeName,
            @RequestParam String attributeValue) {
        List<ProductVariantResponse> variants = productVariantService.getVariantsByProductIdAndAttribute(
                productId, attributeName, attributeValue);
        return ResponseEntity.ok( variants);
    }
}
