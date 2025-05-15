/**
 * Service interface that manages variant attribute operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * variant attributes. Variant attributes define the characteristics that distinguish
 * different variants of a product, such as size, color, or material.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.VariantAttributeResponse;

import java.util.UUID;

public interface VariantAttributeService {
    /**
     * Creates a new variant attribute in the system.
     *
     * @param request The {@link VariantAttributeCreationRequest} containing attribute details
     * @return {@link VariantAttributeResponse} representing the created variant attribute
     */
    VariantAttributeResponse create(VariantAttributeCreationRequest request);

    /**
     * Retrieves a variant attribute by its unique identifier.
     *
     * @param id The UUID of the variant attribute to retrieve
     * @return {@link VariantAttributeResponse} containing the requested variant attribute details
     */
    VariantAttributeResponse findById(UUID id);

    /**
     * Updates an existing variant attribute.
     *
     * @param id The UUID of the variant attribute to update
     * @param request The {@link VariantAttributeUpdateRequest} containing updated attribute details
     * @return {@link VariantAttributeResponse} representing the updated variant attribute
     */
    VariantAttributeResponse update(UUID id, VariantAttributeUpdateRequest request);

    /**
     * Deletes a variant attribute from the system.
     *
     * @param id The UUID of the variant attribute to delete
     */
    void delete(UUID id);
}