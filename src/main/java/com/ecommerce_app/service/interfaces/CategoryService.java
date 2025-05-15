/**
 * Service interface for managing product categories in the e-commerce application.
 * Provides functionality for creating, retrieving, updating, and organizing product categories,
 * including hierarchical category structures.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryBasicResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CategoryTreeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    /**
     * Creates a new product category.
     *
     * @param request The DTO containing category creation details
     * @return The created category as a CategoryResponse
     */
    CategoryResponse createCategory(CategoryCreationRequest request);

    /**
     * Updates an existing product category.
     *
     * @param id The unique identifier of the category to update
     * @param request The DTO containing updated category details
     * @return The updated category as a CategoryResponse
     */
    CategoryResponse updateCategory(UUID id, CategoryUpdateRequest request);

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id The unique identifier of the category
     * @return The category as a CategoryResponse
     */
    CategoryResponse getCategoryById(UUID id);

    /**
     * Retrieves a category by its URL-friendly slug.
     *
     * @param slug The slug identifier of the category
     * @return The category as a CategoryResponse
     */
    CategoryResponse getCategoryBySlug(String slug);

    /**
     * Retrieves all categories with pagination support.
     *
     * @param pageable The pagination information
     * @return A page of categories as CategoryResponse objects
     */
    Page<CategoryResponse> getAllCategories(Pageable pageable);

    /**
     * Retrieves basic information for all categories.
     *
     * @return A list of basic category information as CategoryBasicResponse objects
     */
    List<CategoryBasicResponse> getAllCategoriesBasic();

    /**
     * Retrieves the complete category hierarchy as a tree structure.
     *
     * @return A list of top-level categories with their children as CategoryTreeResponse objects
     */
    List<CategoryTreeResponse> getCategoryTree();

    /**
     * Retrieves all direct subcategories of a specific parent category.
     *
     * @param parentId The unique identifier of the parent category
     * @return A list of subcategories as CategoryResponse objects
     */
    List<CategoryResponse> getSubcategories(UUID parentId);

    /**
     * Deletes a category by its unique identifier.
     *
     * @param id The unique identifier of the category to delete
     */
    void deleteCategory(UUID id);

    /**
     * Moves a category to a new parent category in the hierarchy.
     *
     * @param categoryId The unique identifier of the category to move
     * @param newParentId The unique identifier of the new parent category
     * @return The moved category as a CategoryResponse
     */
    CategoryResponse moveCategory(UUID categoryId, UUID newParentId);
}