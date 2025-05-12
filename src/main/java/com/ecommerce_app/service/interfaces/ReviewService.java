package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ReviewCreationRequest;
import com.ecommerce_app.dto.request.ReviewUpdateRequest;
import com.ecommerce_app.dto.response.ReviewResponse;

import java.util.UUID;

public interface ReviewService {

    /**
     * Creates a new review based on the provided creation request.
     *
     * @param request The review creation request DTO
     * @return The created review response DTO
     * @throws IllegalArgumentException if product or user is not found
     */
    ReviewResponse createReview(ReviewCreationRequest request);

    /**
     * Retrieves a review by its ID.
     *
     * @param id The ID of the review
     * @return The review response DTO
     * @throws IllegalArgumentException if review is not found
     */
    ReviewResponse getReview(UUID id);

    /**
     * Updates an existing review with the provided update request.
     *
     * @param id      The ID of the review to update
     * @param request The review update request DTO
     * @return The updated review response DTO
     * @throws IllegalArgumentException if review is not found
     */
    ReviewResponse updateReview(UUID id, ReviewUpdateRequest request);

    /**
     * Deletes a review by its ID.
     *
     * @param id The ID of the review to delete
     * @throws IllegalArgumentException if review is not found
     */
    void deleteReview(UUID id);
}
