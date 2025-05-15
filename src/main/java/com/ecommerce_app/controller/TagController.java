package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.TagResponse;
import com.ecommerce_app.service.interfaces.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for managing Tag operations.
 * Provides endpoints for creating, retrieving, updating, and deleting tags.
 */
@RestController
@RequestMapping("/tags")
@Tag(name = "Tag", description = "Tag management API")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Creates a new tag.
     *
     * @param request The tag creation request
     * @return ApiResult containing the created tag response
     */
    @PostMapping
    @Operation(summary = "Create a new tag", description = "Creates a new tag with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag successfully created",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<TagResponse> createTag(@Valid @RequestBody TagCreationRequest request) {
        TagResponse response = tagService.createTag(request);
        return ApiResult.success("Tag successfully created", response);
    }

    /**
     * Retrieves a tag by its ID.
     *
     * @param id The ID of the tag to retrieve
     * @return ApiResult containing the tag response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID", description = "Retrieves a tag based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ApiResult<TagResponse> getTag(
            @Parameter(description = "ID of the tag to retrieve") @PathVariable UUID id) {
        TagResponse response = tagService.getTagById(id);
        return ApiResult.success("Tag successfully retrieved", response);
    }

    /**
     * Retrieves all tags.
     *
     * @return ApiResult containing a list of all tag responses
     */
    @GetMapping
    @Operation(summary = "Get all tags", description = "Retrieves a list of all tags")
    @ApiResponse(responseCode = "200", description = "Tags successfully retrieved",
            content = @Content(schema = @Schema(implementation = ApiResult.class)))
    public ApiResult<List<TagResponse>> getAllTags() {
        List<TagResponse> responses = tagService.getAllTags();
        return ApiResult.success("Tags successfully retrieved", responses);
    }

    /**
     * Updates an existing tag.
     *
     * @param id The ID of the tag to update
     * @param request The tag update request
     * @return ApiResult containing the updated tag response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a tag", description = "Updates a tag with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag successfully updated",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ApiResult<TagResponse> updateTag(
            @Parameter(description = "ID of the tag to update") @PathVariable UUID id,
            @Valid @RequestBody TagUpdateRequest request) {
        TagResponse response = tagService.updateTag(id, request);
        return ApiResult.success("Tag successfully updated", response);
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id The ID of the tag to delete
     * @return ApiResult with no data
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag", description = "Deletes a tag with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag successfully deleted",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ApiResult<Void> deleteTag(
            @Parameter(description = "ID of the tag to delete") @PathVariable UUID id) {
        tagService.deleteTag(id);
        return ApiResult.success("Tag successfully deleted", null);
    }
}