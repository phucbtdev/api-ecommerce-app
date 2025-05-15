/**
 * Service interface that manages tag operations in the e-commerce application.
 * <p>
 * This interface provides methods for creating, retrieving, updating, and deleting
 * product tags. Tags are used to categorize and filter products based on various attributes.
 * </p>
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface TagService {
    /**
     * Creates a new tag in the system.
     *
     * @param request The {@link TagCreationRequest} containing tag details
     * @return {@link TagResponse} representing the created tag
     */
    TagResponse createTag(TagCreationRequest request);

    /**
     * Retrieves a tag by its unique identifier.
     *
     * @param id The UUID of the tag to retrieve
     * @return {@link TagResponse} containing the requested tag details
     */
    TagResponse getTagById(UUID id);

    /**
     * Retrieves all tags available in the system.
     *
     * @return A list of {@link TagResponse} objects representing all tags
     */
    List<TagResponse> getAllTags();

    /**
     * Updates an existing tag.
     *
     * @param id The UUID of the tag to update
     * @param request The {@link TagUpdateRequest} containing updated tag details
     * @return {@link TagResponse} representing the updated tag
     */
    TagResponse updateTag(UUID id, TagUpdateRequest request);

    /**
     * Deletes a tag from the system.
     *
     * @param id The UUID of the tag to delete
     */
    void deleteTag(UUID id);
}