package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.VariantAttributeResponse;
import com.ecommerce_app.service.interfaces.VariantAttributeService;
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
 * REST Controller for managing variant attributes.
 * Provides endpoints for creating, retrieving, updating, and deleting variant attributes.
 */
@RestController
@RequestMapping("/variant-attributes")
@RequiredArgsConstructor
@Tag(name = "Variant Attribute", description = "Variant attribute management API")
public class VariantAttributeController {

    private final VariantAttributeService service;

    /**
     * Creates a new variant attribute.
     *
     * @param request The variant attribute creation request
     * @return ApiResult containing the created variant attribute response
     */
    @PostMapping
    @Operation(summary = "Create a new variant attribute", description = "Creates a new variant attribute with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Variant attribute successfully created",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ApiResult<VariantAttributeResponse> create(@Valid @RequestBody VariantAttributeCreationRequest request) {
        VariantAttributeResponse response = service.create(request);
        return ApiResult.success("Variant attribute successfully created", response);
    }

    /**
     * Retrieves a variant attribute by its ID.
     *
     * @param id The ID of the variant attribute to retrieve
     * @return ApiResult containing the variant attribute response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get variant attribute by ID", description = "Retrieves a variant attribute based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant attribute successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ApiResult<VariantAttributeResponse> findById(
            @Parameter(description = "ID of the variant attribute to retrieve") @PathVariable UUID id) {
        VariantAttributeResponse response = service.findById(id);
        return ApiResult.success("Variant attribute successfully retrieved", response);
    }

    /**
     * Updates an existing variant attribute.
     *
     * @param id The ID of the variant attribute to update
     * @param request The variant attribute update request
     * @return ApiResult containing the updated variant attribute response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a variant attribute", description = "Updates a variant attribute with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Variant attribute successfully updated",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ApiResult<VariantAttributeResponse> update(
            @Parameter(description = "ID of the variant attribute to update") @PathVariable UUID id,
            @Valid @RequestBody VariantAttributeUpdateRequest request) {
        VariantAttributeResponse response = service.update(id, request);
        return ApiResult.success("Variant attribute successfully updated", response);
    }

    /**
     * Deletes a variant attribute by its ID.
     *
     * @param id The ID of the variant attribute to delete
     * @return ApiResult with no data
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a variant attribute", description = "Deletes a variant attribute with the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Variant attribute successfully deleted",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Variant attribute not found")
    })
    public ApiResult<Void> delete(
            @Parameter(description = "ID of the variant attribute to delete") @PathVariable UUID id) {
        service.delete(id);
        return ApiResult.success("Variant attribute successfully deleted", null);
    }
}