/**
 * Service interface that manages product operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * products. It includes functionality for searching, filtering, and categorizing products.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    /**
     * Creates a new product in the system.
     *
     * @param request The {@link ProductCreationRequest} containing product details
     * @return {@link ProductResponse} representing the created product
     */
    ProductResponse createProduct(ProductCreationRequest request);

    /**
     * Updates an existing product.
     *
     * @param id The UUID of the product to update
     * @param request The {@link ProductUpdateRequest} containing updated product details
     * @return {@link ProductResponse} representing the updated product
     */
    ProductResponse updateProduct(UUID id, ProductUpdateRequest request);

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The UUID of the product to retrieve
     * @return {@link ProductResponse} containing the requested product details
     */
    ProductResponse getProductById(UUID id);

    /**
     * Retrieves a product by its URL slug.
     *
     * @param slug The URL slug of the product to retrieve
     * @return {@link ProductResponse} containing the requested product details
     */
    ProductResponse getProductBySlug(String slug);

    /**
     * Retrieves all products with pagination support.
     *
     * @param pageable The pagination information
     * @return A page of {@link ProductBasicResponse} objects representing products
     */
    Page<ProductBasicResponse> getAllProducts(Pageable pageable);

    /**
     * Retrieves only active products with pagination support.
     *
     * @param pageable The pagination information
     * @return A page of {@link ProductBasicResponse} objects representing active products
     */
    Page<ProductBasicResponse> getActiveProducts(Pageable pageable);

    /**
     * Retrieves products belonging to a specific category with pagination support.
     *
     * @param categoryId The UUID of the category to filter products by
     * @param pageable The pagination information
     * @return A page of {@link ProductBasicResponse} objects representing products in the category
     */
    Page<ProductBasicResponse> getProductsByCategory(UUID categoryId, Pageable pageable);

    /**
     * Retrieves products associated with a specific tag with pagination support.
     *
     * @param tagId The UUID of the tag to filter products by
     * @param pageable The pagination information
     * @return A page of {@link ProductBasicResponse} objects representing products with the tag
     */
    Page<ProductBasicResponse> getProductsByTag(UUID tagId, Pageable pageable);

    /**
     * Searches for products based on a keyword with pagination support.
     *
     * @param keyword The search keyword
     * @param pageable The pagination information
     * @return A page of {@link ProductBasicResponse} objects representing matching products
     */
    Page<ProductBasicResponse> searchProducts(String keyword, Pageable pageable);

    /**
     * Deletes a product from the system.
     *
     * @param id The UUID of the product to delete
     */
    void deleteProduct(UUID id);

    /**
     * Toggles the active status of a product.
     *
     * @param id The UUID of the product to toggle
     * @return {@code true} if the product is now active, {@code false} if it is now inactive
     */
    boolean toggleProductActive(UUID id);
}