
package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.WishlistCreationRequest;
import com.ecommerce_app.dto.request.WishlistUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.WishlistResponse;
import com.ecommerce_app.service.interfaces.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
 * REST Controller for Wishlist operations.
 * Provides endpoints for creating, retrieving, updating, and deleting wishlists.
 */
@RestController
@RequestMapping("/wishlists")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management API")
public class WishlistController {

    private final WishlistService wishlistService;

    /**
     * Creates a new Wishlist for a user.
     *
     * @param request the creation request
     * @param userId the ID of the user
     * @return ApiResult containing the created Wishlist response
     */
    @PostMapping
    @Operation(summary = "Create a new wishlist", description = "Creates a new wishlist for the specified user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wishlist successfully created",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ApiResult<WishlistResponse> createWishlist(
            @Valid @RequestBody WishlistCreationRequest request,
            @Parameter(description = "ID of the user to create wishlist for") @RequestParam UUID userId) {
        WishlistResponse response = wishlistService.createWishlist(request, userId);
        return ApiResult.success("Wishlist successfully created", response);
    }

    /**
     * Retrieves a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     * @return ApiResult containing the Wishlist response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get wishlist by ID", description = "Retrieves a wishlist based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Wishlist not found")
    })
    public ApiResult<WishlistResponse> getWishlistById(
            @Parameter(description = "ID of the wishlist to retrieve") @PathVariable UUID id) {
        WishlistResponse response = wishlistService.getWishlistById(id);
        return ApiResult.success("Wishlist successfully retrieved", response);
    }

    /**
     * Retrieves a Wishlist by user ID.
     *
     * @param userId the ID of the user
     * @return ApiResult containing the Wishlist response
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get wishlist by user ID", description = "Retrieves a wishlist based on the user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "User not found or wishlist not found for user")
    })
    public ApiResult<WishlistResponse> getWishlistByUserId(
            @Parameter(description = "ID of the user to find wishlist for") @PathVariable UUID userId) {
        WishlistResponse response = wishlistService.getWishlistByUserId(userId);
        return ApiResult.success("Wishlist successfully retrieved", response);
    }

    /**
     * Updates an existing Wishlist.
     *
     * @param id the ID of the Wishlist
     * @param request the update request
     * @return ApiResult containing the updated Wishlist response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a wishlist", description = "Updates a wishlist with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist successfully updated",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found")
    })
    public ApiResult<WishlistResponse> updateWishlist(
            @Parameter(description = "ID of the wishlist to update") @PathVariable UUID id,
            @Valid @RequestBody WishlistUpdateRequest request) {
        WishlistResponse response = wishlistService.updateWishlist(id, request);
        return ApiResult.success("Wishlist successfully updated", response);
    }

    /**
     * Deletes a Wishlist by its ID.
     *
     * @param id the ID of the Wishlist
     * @return ApiResult with no data
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a wishlist", description = "Deletes a wishlist with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist successfully deleted",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Wishlist not found")
    })
    public ApiResult<Void> deleteWishlist(
            @Parameter(description = "ID of the wishlist to delete") @PathVariable UUID id) {
        wishlistService.deleteWishlist(id);
        return ApiResult.success("Wishlist successfully deleted", null);
    }
}