package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.ProductVariantResponse;
import com.ecommerce_app.service.interfaces.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing product variants.
 * Provides endpoints for CRUD operations on product variants.
 */
@RestController
@RequestMapping("/products/{productId}/variants")
@RequiredArgsConstructor
@Tag(name = "Product Variant", description = "Product variant management APIs")
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    /**
     * Creates a new product variant for a specific product.
     *
     * @param productId The ID of the product to create variant for
     * @param request The product variant creation request
     * @return API result containing the created product variant
     */
    @PostMapping
    @Operation(summary = "Create a new product variant", description = "Creates a new variant for the specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ApiResult<ProductVariantResponse> createProductVariant(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductVariantCreationRequest request) {
        ProductVariantResponse variant = productVariantService.createProductVariant(productId, request);
        return ApiResult.success("Product variant created successfully", variant);
    }

    /**
     * Retrieves all variants for a specific product.
     *
     * @param productId The ID of the product
     * @return API result containing list of product variants
     */
    @GetMapping
    @Operation(summary = "Get all variants for a product", description = "Retrieves all variants associated with the specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variants retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ApiResult<List<ProductVariantResponse>> getProductVariants(
            @PathVariable UUID productId) {
        List<ProductVariantResponse> variants = productVariantService.getVariantsByProductId(productId);
        return ApiResult.success("Product variants retrieved successfully", variants);
    }

    /**
     * Retrieves a specific product variant by ID.
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant
     * @return API result containing the product variant
     */
    @GetMapping("/{variantId}")
    @Operation(summary = "Get product variant by ID", description = "Retrieves a specific product variant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Variant or product not found")
    })
    public ApiResult<ProductVariantResponse> getProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId) {
        ProductVariantResponse variant = productVariantService.getProductVariantById(variantId);
        return ApiResult.success("Product variant retrieved successfully", variant);
    }

    /**
     * Updates an existing product variant.
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to update
     * @param request The product variant update request
     * @return API result containing the updated product variant
     */
    @PutMapping("/{variantId}")
    @Operation(summary = "Update a product variant", description = "Updates an existing product variant with new information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Variant or product not found")
    })
    public ApiResult<ProductVariantResponse> updateProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId,
            @Valid @RequestBody ProductVariantUpdateRequest request) {
        ProductVariantResponse variant = productVariantService.updateProductVariant(variantId, request);
        return ApiResult.success("Product variant updated successfully", variant);
    }

    /**
     * Deletes a product variant by ID.
     *
     * @param productId The ID of the product
     * @param variantId The ID of the variant to delete
     * @return API result with no data content
     */
    @DeleteMapping("/{variantId}")
    @Operation(summary = "Delete a product variant", description = "Deletes a specific product variant by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Variant or product not found")
    })
    public ApiResult<Void> deleteProductVariant(
            @PathVariable UUID productId,
            @PathVariable UUID variantId) {
        productVariantService.deleteProductVariant(variantId);
        return ApiResult.success("Product variant deleted successfully", null);
    }

    /**
     * Filters product variants by attribute name and value.
     *
     * @param productId The ID of the product
     * @param attributeName The name of the attribute to filter by
     * @param attributeValue The value of the attribute to filter by
     * @return API result containing filtered product variants
     */
    @GetMapping("/filter")
    @Operation(summary = "Filter product variants by attribute",
            description = "Retrieves product variants filtered by specific attribute name and value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variants filtered successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ApiResult<List<ProductVariantResponse>> filterVariantsByAttribute(
            @PathVariable UUID productId,
            @RequestParam String attributeName,
            @RequestParam String attributeValue) {
        List<ProductVariantResponse> variants = productVariantService.getVariantsByProductIdAndAttribute(
                productId, attributeName, attributeValue);
        return ApiResult.success("Product variants filtered successfully", variants);
    }
}