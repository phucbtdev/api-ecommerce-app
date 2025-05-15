/**
 * Service interface that manages product image operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * product images. It handles images at both the product level and the variant level.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ProductImageResponse;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {
    /**
     * Creates a new product image associated with a specific product.
     *
     * @param productId The UUID of the product to associate the image with
     * @param request The {@link ProductImageCreationRequest} containing image details
     * @return {@link ProductImageResponse} representing the created product image
     */
    ProductImageResponse createProductImage(UUID productId, ProductImageCreationRequest request);

    /**
     * Updates an existing product image.
     *
     * @param id The UUID of the product image to update
     * @param request The {@link ProductImageUpdateRequest} containing updated image details
     * @return {@link ProductImageResponse} representing the updated product image
     */
    ProductImageResponse updateProductImage(UUID id, ProductImageUpdateRequest request);

    /**
     * Retrieves a product image by its unique identifier.
     *
     * @param id The UUID of the product image to retrieve
     * @return {@link ProductImageResponse} containing the requested product image details
     */
    ProductImageResponse getProductImageById(UUID id);

    /**
     * Retrieves all images associated with a specific product.
     *
     * @param productId The UUID of the product to get images for
     * @return A list of {@link ProductImageResponse} objects representing all images for the product
     */
    List<ProductImageResponse> getImagesByProductId(UUID productId);

    /**
     * Retrieves all images associated with a specific product variant.
     *
     * @param variantId The UUID of the product variant to get images for
     * @return A list of {@link ProductImageResponse} objects representing all images for the variant
     */
    List<ProductImageResponse> getImagesByVariantId(UUID variantId);

    /**
     * Retrieves images that are associated with the product itself, not with any specific variant.
     *
     * @param productId The UUID of the product to get product-level images for
     * @return A list of {@link ProductImageResponse} objects representing product-level images
     */
    List<ProductImageResponse> getProductLevelImages(UUID productId);

    /**
     * Deletes a product image from the system.
     *
     * @param id The UUID of the product image to delete
     */
    void deleteProductImage(UUID id);

    /**
     * Sets a specific image as the main image for its associated product or variant.
     *
     * @param imageId The UUID of the image to set as main
     * @return {@link ProductImageResponse} representing the updated product image
     */
    ProductImageResponse setMainImage(UUID imageId);
}