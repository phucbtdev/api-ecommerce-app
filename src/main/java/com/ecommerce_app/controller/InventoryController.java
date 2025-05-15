package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.InventoryResponse;
import com.ecommerce_app.service.interfaces.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing inventory operations.
 * Provides endpoints for creating, retrieving, updating, and deleting inventory records,
 * as well as specialized operations for managing stock quantities and reservations.
 */
@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management API")
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Creates a new inventory record.
     *
     * @param request The inventory creation request containing necessary information
     * @return The created inventory information
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Create a new inventory record", description = "Creates a new inventory record with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to create inventory")
    })
    public ApiResult<InventoryResponse> createInventory(
            @Parameter(description = "Inventory creation details", required = true)
            @Valid @RequestBody InventoryCreationRequest request) {
        return ApiResult.success("Inventory created successfully",
                inventoryService.createInventory(request));
    }

    /**
     * Retrieves an inventory record by its ID.
     *
     * @param id The UUID of the inventory to retrieve
     * @return The inventory information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get inventory by ID", description = "Retrieves inventory information for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventory")
    })
    public ApiResult<InventoryResponse> getInventoryById(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id) {
        return ApiResult.success("Inventory retrieved successfully",
                inventoryService.getInventoryById(id));
    }

    /**
     * Retrieves an inventory record by product variant ID.
     *
     * @param productVariantId The UUID of the product variant
     * @return The inventory information
     */
    @GetMapping("/product-variant/{productVariantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get inventory by product variant ID",
            description = "Retrieves inventory information for the specified product variant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found for product variant",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventory")
    })
    public ApiResult<InventoryResponse> getInventoryByProductVariantId(
            @Parameter(description = "Product variant ID", required = true)
            @PathVariable UUID productVariantId) {
        return ApiResult.success("Inventory retrieved successfully",
                inventoryService.getInventoryByProductVariantId(productVariantId));
    }

    /**
     * Retrieves an inventory record by SKU.
     *
     * @param sku The SKU of the inventory
     * @return The inventory information
     */
    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get inventory by SKU",
            description = "Retrieves inventory information for the specified SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found for SKU",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventory")
    })
    public ApiResult<InventoryResponse> getInventoryBySku(
            @Parameter(description = "Inventory SKU", required = true)
            @PathVariable String sku) {
        return ApiResult.success("Inventory retrieved successfully",
                inventoryService.getInventoryBySku(sku));
    }

    /**
     * Retrieves an inventory record by product variant SKU.
     *
     * @param sku The SKU of the product variant
     * @return The inventory information
     */
    @GetMapping("/product-variant-sku/{sku}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get inventory by product variant SKU",
            description = "Retrieves inventory information for the specified product variant SKU")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found for product variant SKU",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventory")
    })
    public ApiResult<InventoryResponse> getInventoryByProductVariantSku(
            @Parameter(description = "Product variant SKU", required = true)
            @PathVariable String sku) {
        return ApiResult.success("Inventory retrieved successfully",
                inventoryService.getInventoryByProductVariantSku(sku));
    }

    /**
     * Retrieves all inventory records.
     *
     * @return List of all inventory records
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get all inventories", description = "Retrieves all inventory records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventories")
    })
    public ApiResult<List<InventoryResponse>> getAllInventories() {
        return ApiResult.success("All inventories retrieved successfully",
                inventoryService.getAllInventories());
    }

    /**
     * Retrieves all inventory records with pagination.
     *
     * @param pageable Pagination information
     * @return Page of inventory records
     */
    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get all inventories with pagination",
            description = "Retrieves all inventory records with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventories retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access inventories")
    })
    public ApiResult<Page<InventoryResponse>> getAllInventoriesPaged(
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return ApiResult.success("Paged inventories retrieved successfully",
                inventoryService.getAllInventories(pageable));
    }

    /**
     * Updates an existing inventory record.
     *
     * @param id The UUID of the inventory to update
     * @param request The update request containing the new information
     * @return The updated inventory information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Update inventory", description = "Updates an existing inventory record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to update inventory")
    })
    public ApiResult<InventoryResponse> updateInventory(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Inventory update details", required = true)
            @Valid @RequestBody InventoryUpdateRequest request) {
        return ApiResult.success("Inventory updated successfully",
                inventoryService.updateInventory(id, request));
    }

    /**
     * Deletes an inventory record.
     *
     * @param id The UUID of the inventory to delete
     * @return Empty response with no content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Delete inventory", description = "Deletes an existing inventory record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to delete inventory")
    })
    public ApiResult<Void> deleteInventory(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id) {
        inventoryService.deleteInventory(id);
        return ApiResult.success("Inventory deleted successfully", null);
    }

    /**
     * Updates the stock quantity of an inventory record.
     *
     * @param id The UUID of the inventory to update
     * @param quantity The new stock quantity
     * @return The updated inventory information
     */
    @PatchMapping("/{id}/stock-quantity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Update stock quantity",
            description = "Updates the stock quantity of an existing inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock quantity updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quantity value",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to update stock")
    })
    public ApiResult<InventoryResponse> updateStockQuantity(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "New stock quantity", required = true)
            @RequestParam Integer quantity) {
        return ApiResult.success("Stock quantity updated successfully",
                inventoryService.updateStockQuantity(id, quantity));
    }

    /**
     * Reserves stock from inventory.
     *
     * @param id The UUID of the inventory from which to reserve
     * @param quantity The quantity to reserve
     * @return The updated inventory information
     */
    @PatchMapping("/{id}/reserve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Reserve stock", description = "Reserves a specified quantity from inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock reserved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient stock",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to reserve stock")
    })
    public ApiResult<InventoryResponse> reserveStock(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Quantity to reserve", required = true)
            @RequestParam Integer quantity) {
        return ApiResult.success("Stock reserved successfully",
                inventoryService.reserveStock(id, quantity));
    }

    /**
     * Releases previously reserved stock back to available inventory.
     *
     * @param id The UUID of the inventory
     * @param quantity The quantity to release from reservation
     * @return The updated inventory information
     */
    @PatchMapping("/{id}/release-reservation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Release reserved stock",
            description = "Releases previously reserved stock back to available inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserved stock released successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient reserved stock",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to release stock")
    })
    public ApiResult<InventoryResponse> releaseReservedStock(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Quantity to release from reservation", required = true)
            @RequestParam Integer quantity) {
        return ApiResult.success("Reserved stock released successfully",
                inventoryService.releaseReservedStock(id, quantity));
    }

    /**
     * Commits reserved stock, reducing it from total inventory.
     *
     * @param id The UUID of the inventory
     * @param quantity The quantity to commit from reservation
     * @return The updated inventory information
     */
    @PatchMapping("/{id}/commit-reservation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Commit reserved stock",
            description = "Commits reserved stock, reducing it from total inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserved stock committed successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid quantity or insufficient reserved stock",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to commit stock")
    })
    public ApiResult<InventoryResponse> commitReservedStock(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Quantity to commit from reservation", required = true)
            @RequestParam Integer quantity) {
        return ApiResult.success("Reserved stock committed successfully",
                inventoryService.commitReservedStock(id, quantity));
    }
}