package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing product operations.
 * Provides endpoints for creating, updating, retrieving, and deleting products.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API for product management")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param request The product details to create
     * @return ApiResult containing the created product information
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    public ApiResult<ProductResponse> createProduct(@Valid @RequestBody ProductCreationRequest request) {
        ProductResponse product = productService.createProduct(request);
        return ApiResult.success("Product created successfully", product);
    }

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update
     * @param request The product details to update
     * @return ApiResult containing the updated product information
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product", description = "Updates a product with the provided details")
    public ApiResult<ProductResponse> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse product = productService.updateProduct(id, request);
        return ApiResult.success("Product updated successfully", product);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return ApiResult containing the product information
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves product information for the specified ID")
    public ApiResult<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse product = productService.getProductById(id);
        return ApiResult.success("Product retrieved successfully", product);
    }

    /**
     * Retrieves a product by its slug.
     *
     * @param slug The slug of the product to retrieve
     * @return ApiResult containing the product information
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get product by slug", description = "Retrieves product information for the specified slug")
    public ApiResult<ProductResponse> getProductBySlug(@PathVariable String slug) {
        ProductResponse product = productService.getProductBySlug(slug);
        return ApiResult.success("Product retrieved successfully", product);
    }

    /**
     * Retrieves all products with pagination.
     *
     * @param page The page number
     * @param size The page size
     * @param sort The sort field
     * @param direction The sort direction
     * @return ApiResult containing a page of products
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products with pagination")
    public ApiResult<Page<ProductBasicResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductBasicResponse> products = productService.getAllProducts(pageable);
        return ApiResult.success("Products retrieved successfully", products);
    }

    /**
     * Retrieves active products with pagination.
     *
     * @param page The page number
     * @param size The page size
     * @param sort The sort field
     * @param direction The sort direction
     * @return ApiResult containing a page of active products
     */
    @GetMapping("/active")
    @Operation(summary = "Get active products", description = "Retrieves all active products with pagination")
    public ApiResult<Page<ProductBasicResponse>> getActiveProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductBasicResponse> products = productService.getActiveProducts(pageable);
        return ApiResult.success("Active products retrieved successfully", products);
    }

    /**
     * Retrieves products by category with pagination.
     *
     * @param categoryId The ID of the category
     * @param page The page number
     * @param size The page size
     * @param sort The sort field
     * @param direction The sort direction
     * @return ApiResult containing a page of products in the specified category
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Retrieves products for the specified category with pagination")
    public ApiResult<Page<ProductBasicResponse>> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductBasicResponse> products = productService.getProductsByCategory(categoryId, pageable);
        return ApiResult.success("Products by category retrieved successfully", products);
    }

    /**
     * Retrieves products by tag with pagination.
     *
     * @param tagId The ID of the tag
     * @param page The page number
     * @param size The page size
     * @param sort The sort field
     * @param direction The sort direction
     * @return ApiResult containing a page of products with the specified tag
     */
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "Get products by tag", description = "Retrieves products for the specified tag with pagination")
    public ApiResult<Page<ProductBasicResponse>> getProductsByTag(
            @PathVariable UUID tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductBasicResponse> products = productService.getProductsByTag(tagId, pageable);
        return ApiResult.success("Products by tag retrieved successfully", products);
    }

    /**
     * Searches for products with pagination.
     *
     * @param keyword The search keyword
     * @param page The page number
     * @param size The page size
     * @param sort The sort field
     * @param direction The sort direction
     * @return ApiResult containing a page of products matching the search keyword
     */
    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Searches for products matching the keyword with pagination")
    public ApiResult<Page<ProductBasicResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
        Page<ProductBasicResponse> products = productService.searchProducts(keyword, pageable);
        return ApiResult.success("Product search results retrieved successfully", products);
    }

    /**
     * Deletes a product.
     *
     * @param id The ID of the product to delete
     * @return ApiResult with a success message
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes the product with the specified ID")
    public ApiResult<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ApiResult.success("Product deleted successfully", null);
    }

    /**
     * Toggles the active status of a product.
     *
     * @param id The ID of the product to toggle
     * @return ApiResult containing the new active status
     */
    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle product active status", description = "Toggles the active status of the product with the specified ID")
    public ApiResult<Boolean> toggleProductActive(@PathVariable UUID id) {
        Boolean activeStatus = productService.toggleProductActive(id);
        return ApiResult.success("Product active status toggled successfully", activeStatus);
    }
}