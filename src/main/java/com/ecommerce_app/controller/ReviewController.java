package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ReviewCreationRequest;
import com.ecommerce_app.dto.request.ReviewUpdateRequest;
import com.ecommerce_app.dto.response.ReviewResponse;
import com.ecommerce_app.service.interfaces.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Creates a new review.
     *
     * @param request The review creation request
     * @return The created review response
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreationRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a review by ID.
     *
     * @param id The ID of the review
     * @return The review response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID id) {
        ReviewResponse response = reviewService.getReview(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing review.
     *
     * @param id      The ID of the review
     * @param request The review update request
     * @return The updated review response
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable UUID id,
                                                       @Valid @RequestBody ReviewUpdateRequest request) {
        ReviewResponse response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a review by ID.
     *
     * @param id The ID of the review
     * @return No content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}