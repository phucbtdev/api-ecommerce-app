/**
 * Service interface that manages product variant operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * product variants. It handles variant attributes and facilitates variant filtering.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.ProductVariantResponse;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {
    /**
     * Creates a new product variant associated with a specific product.
     *
     * @param productId The UUID of the product to associate the variant with
     * @param request The {@link ProductVariantCreationRequest} containing variant details
     * @return {@link ProductVariantResponse} representing the created product variant
     */
    ProductVariantResponse createProductVariant(UUID productId, ProductVariantCreationRequest request);

    /**
     * Updates an existing product variant.
     *
     * @param id The UUID of the product variant to update
     * @param request The {@link ProductVariantUpdateRequest} containing updated variant details
     * @return {@link ProductVariantResponse} representing the updated product variant
     */
    ProductVariantResponse updateProductVariant(UUID id, ProductVariantUpdateRequest request);

    /**
     * Retrieves a product variant by its unique identifier.
     *
     * @param id The UUID of the product variant to retrieve
     * @return {@link ProductVariantResponse} containing the requested product variant details
     */
    ProductVariantResponse getProductVariantById(UUID id);

    /**
     * Retrieves all variants associated with a specific product.
     *
     * @param productId The UUID of the product to get variants for
     * @return A list of {@link ProductVariantResponse} objects representing all variants for the product
     */
    List<ProductVariantResponse> getVariantsByProductId(UUID productId);

    /**
     * Deletes a product variant from the system.
     *
     * @param id The UUID of the product variant to delete
     */
    void deleteProductVariant(UUID id);

    /**
     * Retrieves product variants that match a specific attribute name and value for a given product.
     *
     * @param productId The UUID of the product to filter variants for
     * @param attributeName The name of the attribute to filter by
     * @param attributeValue The value of the attribute to filter by
     * @return A list of {@link ProductVariantResponse} objects representing matching variants
     */
    List<ProductVariantResponse> getVariantsByProductIdAndAttribute(
            UUID productId, String attributeName, String attributeValue);
}