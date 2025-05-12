package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ProductImageResponse;
import com.ecommerce_app.service.interfaces.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    /**
     * Create a new product image
     * @param productId ID of the product
     * @param request Image creation details
     * @return Created product image
     */
    @PostMapping("/products/{productId}/images")
    public ResponseEntity<ProductImageResponse> createProductImage(
            @PathVariable UUID productId,
            @RequestBody ProductImageCreationRequest request) {
        ProductImageResponse response = productImageService.createProductImage(productId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing product image
     * @param id ID of the image to update
     * @param request Image update details
     * @return Updated product image
     */
    @PutMapping("/product-images/{id}")
    public ResponseEntity<ProductImageResponse> updateProductImage(
            @PathVariable UUID id,
            @RequestBody ProductImageUpdateRequest request) {
        ProductImageResponse response = productImageService.updateProductImage(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a product image by ID
     * @param id ID of the image
     * @return Product image details
     */
    @GetMapping("/product-images/{id}")
    public ResponseEntity<ProductImageResponse> getProductImageById(@PathVariable UUID id) {
        ProductImageResponse response = productImageService.getProductImageById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all images for a specific product
     * @param productId ID of the product
     * @return List of product images
     */
    @GetMapping("/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getImagesByProductId(@PathVariable UUID productId) {
        List<ProductImageResponse> images = productImageService.getImagesByProductId(productId);
        return ResponseEntity.ok(images);
    }

    /**
     * Get only product-level images (not variant-specific) for a product
     * @param productId ID of the product
     * @return List of product-level images
     */
    @GetMapping("/products/{productId}/product-level-images")
    public ResponseEntity<List<ProductImageResponse>> getProductLevelImages(@PathVariable UUID productId) {
        List<ProductImageResponse> images = productImageService.getProductLevelImages(productId);
        return ResponseEntity.ok(images);
    }

    /**
     * Get all images for a specific product variant
     * @param variantId ID of the product variant
     * @return List of variant images
     */
    @GetMapping("/product-variants/{variantId}/images")
    public ResponseEntity<List<ProductImageResponse>> getImagesByVariantId(@PathVariable UUID variantId) {
        List<ProductImageResponse> images = productImageService.getImagesByVariantId(variantId);
        return ResponseEntity.ok(images);
    }

    /**
     * Delete a product image
     * @param id ID of the image to delete
     * @return No content response
     */
    @DeleteMapping("/product-images/{id}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable UUID id) {
        productImageService.deleteProductImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Set an image as the main image for its product
     * @param id ID of the image to set as main
     * @return Updated product image
     */
    @PutMapping("/product-images/{id}/set-main")
    public ResponseEntity<ProductImageResponse> setMainImage(@PathVariable UUID id) {
        ProductImageResponse response = productImageService.setMainImage(id);
        return ResponseEntity.ok(response);
    }
}
