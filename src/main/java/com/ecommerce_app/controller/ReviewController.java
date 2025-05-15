package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.ReviewCreationRequest;
import com.ecommerce_app.dto.request.ReviewUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.ReviewResponse;
import com.ecommerce_app.service.interfaces.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing product reviews.
 * Provides endpoints for CRUD operations on reviews.
 */
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Product review management APIs")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Creates a new review.
     *
     * @param request The review creation request
     * @return API result containing the created review
     */
    @PostMapping
    @Operation(summary = "Create a new review", description = "Creates a new review for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<ReviewResponse> createReview(@Valid @RequestBody ReviewCreationRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return ApiResult.success("Review created successfully", response);
    }

    /**
     * Retrieves a review by ID.
     *
     * @param id The ID of the review
     * @return API result containing the review
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieves a specific review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ApiResult<ReviewResponse> getReview(@PathVariable UUID id) {
        ReviewResponse response = reviewService.getReview(id);
        return ApiResult.success("Review retrieved successfully", response);
    }

    /**
     * Updates an existing review.
     *
     * @param id      The ID of the review
     * @param request The review update request
     * @return API result containing the updated review
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a review", description = "Updates an existing review with new information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ApiResult<ReviewResponse> updateReview(@PathVariable UUID id,
                                                  @Valid @RequestBody ReviewUpdateRequest request) {
        ReviewResponse response = reviewService.updateReview(id, request);
        return ApiResult.success("Review updated successfully", response);
    }

    /**
     * Deletes a review by ID.
     *
     * @param id The ID of the review
     * @return API result with no data content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Deletes a specific review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ApiResult<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ApiResult.success("Review deleted successfully", null);
    }
}