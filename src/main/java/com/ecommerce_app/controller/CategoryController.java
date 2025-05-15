package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.CategoryBasicResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CategoryTreeResponse;
import com.ecommerce_app.service.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing product categories.
 * This controller provides endpoints for creating, reading, updating, and deleting categories.
 * Categories can be organized in a hierarchical tree structure with parent-child relationships.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Management", description = "APIs for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new category.
     *
     * @param request Data transfer object containing category creation information
     * @return ApiResult containing the created category response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    @Operation(summary = "Create a new category", description = "Creates a new product category. Requires CATEGORY_MANAGE authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryCreationRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return ApiResult.success("Category created successfully!", createdCategory);
    }

    /**
     * Updates an existing category.
     *
     * @param id The UUID of the category to update
     * @param request Data transfer object containing category update information
     * @return ApiResult containing the updated category response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    @Operation(summary = "Update a category", description = "Updates an existing category. Requires CATEGORY_MANAGE authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<CategoryResponse> updateCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        return ApiResult.success("Category updated successfully!", updatedCategory);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The UUID of the category to retrieve
     * @return ApiResult containing the category response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves detailed information about a category by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ApiResult<CategoryResponse> getCategoryById(@Parameter(description = "Category ID") @PathVariable UUID id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ApiResult.success("Category retrieved successfully!", category);
    }

    /**
     * Retrieves a category by its slug.
     *
     * @param slug The slug of the category to retrieve
     * @return ApiResult containing the category response
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get category by slug", description = "Retrieves detailed information about a category by its slug.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ApiResult<CategoryResponse> getCategoryBySlug(@Parameter(description = "Category slug") @PathVariable String slug) {
        CategoryResponse category = categoryService.getCategoryBySlug(slug);
        return ApiResult.success("Category retrieved successfully!", category);
    }

    /**
     * Retrieves a paginated list of all categories.
     *
     * @param pageable Pagination and sorting information
     * @return ApiResult containing the paginated category responses
     */
    @GetMapping
    @Operation(summary = "Get all categories paginated", description = "Retrieves a paginated list of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<Page<CategoryResponse>> getAllCategories(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.getAllCategories(pageable);
        return ApiResult.success("Categories retrieved successfully!", categories);
    }

    /**
     * Retrieves a list of basic category information for all categories.
     *
     * @return ApiResult containing the list of basic category responses
     */
    @GetMapping("/basic")
    @Operation(summary = "Get all categories (basic information)", description = "Retrieves a list of all categories with basic information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Basic category information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CategoryBasicResponse>> getAllCategoriesBasic() {
        List<CategoryBasicResponse> categories = categoryService.getAllCategoriesBasic();
        return ApiResult.success("Basic category information retrieved successfully!", categories);
    }

    /**
     * Retrieves the category hierarchy as a tree structure.
     *
     * @return ApiResult containing the category tree
     */
    @GetMapping("/tree")
    @Operation(summary = "Get category tree", description = "Retrieves the hierarchical tree structure of all categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category tree retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<List<CategoryTreeResponse>> getCategoryTree() {
        List<CategoryTreeResponse> categoryTree = categoryService.getCategoryTree();
        return ApiResult.success("Category tree retrieved successfully!", categoryTree);
    }

    /**
     * Retrieves all subcategories of a specified parent category.
     *
     * @param parentId The UUID of the parent category
     * @return ApiResult containing the list of subcategory responses
     */
    @GetMapping("/{parentId}/subcategories")
    @Operation(summary = "Get subcategories", description = "Retrieves all subcategories of a specified parent category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subcategories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Parent category not found")
    })
    public ApiResult<List<CategoryResponse>> getSubcategories(@Parameter(description = "Parent category ID") @PathVariable UUID parentId) {
        List<CategoryResponse> subcategories = categoryService.getSubcategories(parentId);
        return ApiResult.success("Subcategories retrieved successfully!", subcategories);
    }

    /**
     * Deletes a category.
     *
     * @param id The UUID of the category to delete
     * @return ApiResult with no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    @Operation(summary = "Delete a category", description = "Deletes a category. Requires CATEGORY_MANAGE authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<Void> deleteCategory(@Parameter(description = "Category ID") @PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ApiResult.success("Category deleted successfully!", null);
    }

    /**
     * Moves a category to a new parent category or to the root level.
     *
     * @param id The UUID of the category to move
     * @param parentId The UUID of the new parent category (null for root level)
     * @return ApiResult containing the updated category response
     */
    @PatchMapping("/{id}/move")
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    @Operation(summary = "Move a category", description = "Moves a category to a new parent or to the root level. Requires CATEGORY_MANAGE authority.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category moved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "400", description = "Invalid parent category"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ApiResult<CategoryResponse> moveCategory(
            @Parameter(description = "Category ID") @PathVariable UUID id,
            @Parameter(description = "New parent category ID (omit for root level)") @RequestParam(required = false) UUID parentId) {
        CategoryResponse movedCategory = categoryService.moveCategory(id, parentId);
        return ApiResult.success("Category moved successfully!", movedCategory);
    }
}