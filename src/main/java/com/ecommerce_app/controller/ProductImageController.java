package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.ProductImageResponse;
import com.ecommerce_app.service.interfaces.ProductImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing product images.
 * Provides endpoints for creating, updating, retrieving, and deleting product images.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Product Images", description = "API for product image management")
public class ProductImageController {

    private final ProductImageService productImageService;

    /**
     * Creates a new product image.
     *
     * @param productId ID of the product
     * @param request Image creation details
     * @return ApiResult containing the created product image
     */
    @PostMapping("/products/{productId}/images")
    @Operation(summary = "Create a new product image", description = "Adds a new image to the specified product")
    public ApiResult<ProductImageResponse> createProductImage(
            @PathVariable UUID productId,
            @RequestBody ProductImageCreationRequest request) {
        ProductImageResponse response = productImageService.createProductImage(productId, request);
        return ApiResult.success("Product image created successfully", response);
    }

    /**
     * Updates an existing product image.
     *
     * @param id ID of the image to update
     * @param request Image update details
     * @return ApiResult containing the updated product image
     */
    @PutMapping("/product-images/{id}")
    @Operation(summary = "Update a product image", description = "Updates an existing product image with the provided details")
    public ApiResult<ProductImageResponse> updateProductImage(
            @PathVariable UUID id,
            @RequestBody ProductImageUpdateRequest request) {
        ProductImageResponse response = productImageService.updateProductImage(id, request);
        return ApiResult.success("Product image updated successfully", response);
    }

    /**
     * Gets a product image by ID.
     *
     * @param id ID of the image
     * @return ApiResult containing the product image details
     */
    @GetMapping("/product-images/{id}")
    @Operation(summary = "Get product image by ID", description = "Retrieves product image information for the specified ID")
    public ApiResult<ProductImageResponse> getProductImageById(@PathVariable UUID id) {
        ProductImageResponse response = productImageService.getProductImageById(id);
        return ApiResult.success("Product image retrieved successfully", response);
    }

    /**
     * Gets all images for a specific product.
     *
     * @param productId ID of the product
     * @return ApiResult containing a list of product images
     */
    @GetMapping("/products/{productId}/images")
    @Operation(summary = "Get all images for a product", description = "Retrieves all images associated with the specified product")
    public ApiResult<List<ProductImageResponse>> getImagesByProductId(@PathVariable UUID productId) {
        List<ProductImageResponse> images = productImageService.getImagesByProductId(productId);
        return ApiResult.success("Product images retrieved successfully", images);
    }

    /**
     * Gets only product-level images (not variant-specific) for a product.
     *
     * @param productId ID of the product
     * @return ApiResult containing a list of product-level images
     */
    @GetMapping("/products/{productId}/product-level-images")
    @Operation(summary = "Get product-level images", description = "Retrieves only product-level images (not variant-specific) for the specified product")
    public ApiResult<List<ProductImageResponse>> getProductLevelImages(@PathVariable UUID productId) {
        List<ProductImageResponse> images = productImageService.getProductLevelImages(productId);
        return ApiResult.success("Product-level images retrieved successfully", images);
    }

    /**
     * Gets all images for a specific product variant.
     *
     * @param variantId ID of the product variant
     * @return ApiResult containing a list of variant images
     */
    @GetMapping("/product-variants/{variantId}/images")
    @Operation(summary = "Get variant images", description = "Retrieves all images associated with the specified product variant")
    public ApiResult<List<ProductImageResponse>> getImagesByVariantId(@PathVariable UUID variantId) {
        List<ProductImageResponse> images = productImageService.getImagesByVariantId(variantId);
        return ApiResult.success("Variant images retrieved successfully", images);
    }

    /**
     * Deletes a product image.
     *
     * @param id ID of the image to delete
     * @return ApiResult with a success message
     */
    @DeleteMapping("/product-images/{id}")
    @Operation(summary = "Delete a product image", description = "Deletes the product image with the specified ID")
    public ApiResult<Void> deleteProductImage(@PathVariable UUID id) {
        productImageService.deleteProductImage(id);
        return ApiResult.success("Product image deleted successfully", null);
    }

    /**
     * Sets an image as the main image for its product.
     *
     * @param id ID of the image to set as main
     * @return ApiResult containing the updated product image
     */
    @PutMapping("/product-images/{id}/set-main")
    @Operation(summary = "Set main image", description = "Sets the specified image as the main image for its associated product")
    public ApiResult<ProductImageResponse> setMainImage(@PathVariable UUID id) {
        ProductImageResponse response = productImageService.setMainImage(id);
        return ApiResult.success("Image set as main successfully", response);
    }
}