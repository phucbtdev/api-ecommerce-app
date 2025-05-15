/**
 * Service interface for managing product inventory in the e-commerce application.
 * Provides functionality for creating, retrieving, updating inventory records,
 * and handling stock adjustments, reservations, and transactions.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    /**
     * Creates a new inventory record.
     *
     * @param request The DTO containing inventory creation details
     * @return The created inventory as an InventoryResponse
     */
    InventoryResponse createInventory(InventoryCreationRequest request);

    /**
     * Retrieves an inventory record by its unique identifier.
     *
     * @param id The unique identifier of the inventory record
     * @return The inventory record as an InventoryResponse
     */
    InventoryResponse getInventoryById(UUID id);

    /**
     * Retrieves an inventory record by its associated product variant ID.
     *
     * @param productVariantId The unique identifier of the product variant
     * @return The inventory record as an InventoryResponse
     */
    InventoryResponse getInventoryByProductVariantId(UUID productVariantId);

    /**
     * Retrieves an inventory record by its SKU (Stock Keeping Unit).
     *
     * @param sku The SKU of the inventory item
     * @return The inventory record as an InventoryResponse
     */
    InventoryResponse getInventoryBySku(String sku);

    /**
     * Retrieves an inventory record by the SKU of its associated product variant.
     *
     * @param sku The SKU of the product variant
     * @return The inventory record as an InventoryResponse
     */
    InventoryResponse getInventoryByProductVariantSku(String sku);

    /**
     * Retrieves all inventory records.
     *
     * @return A list of all inventory records as InventoryResponse objects
     */
    List<InventoryResponse> getAllInventories();

    /**
     * Retrieves all inventory records with pagination support.
     *
     * @param pageable The pagination information
     * @return A page of inventory records as InventoryResponse objects
     */
    Page<InventoryResponse> getAllInventories(Pageable pageable);

    /**
     * Updates an existing inventory record.
     *
     * @param id The unique identifier of the inventory record to update
     * @param request The DTO containing updated inventory details
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse updateInventory(UUID id, InventoryUpdateRequest request);

    /**
     * Deletes an inventory record by its unique identifier.
     *
     * @param id The unique identifier of the inventory record to delete
     */
    void deleteInventory(UUID id);

    /**
     * Adjusts the stock quantity of an inventory item by a relative amount.
     *
     * @param id The unique identifier of the inventory record
     * @param quantity The quantity to adjust (positive for increase, negative for decrease)
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse adjustStockQuantity(UUID id, Integer quantity);

    /**
     * Sets the stock quantity of an inventory item to a specific value.
     *
     * @param id The unique identifier of the inventory record
     * @param stockQuantity The new stock quantity
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse updateStockQuantity(UUID id, Integer stockQuantity);

    /**
     * Reserves a quantity of stock for an order being processed.
     *
     * @param id The unique identifier of the inventory record
     * @param quantity The quantity to reserve
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse reserveStock(UUID id, Integer quantity);

    /**
     * Releases previously reserved stock back to available inventory.
     *
     * @param id The unique identifier of the inventory record
     * @param quantity The quantity to release from reservation
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse releaseReservedStock(UUID id, Integer quantity);

    /**
     * Commits reserved stock to completed sales, removing it from inventory.
     *
     * @param id The unique identifier of the inventory record
     * @param quantity The quantity to commit from the reserved stock
     * @return The updated inventory record as an InventoryResponse
     */
    InventoryResponse commitReservedStock(UUID id, Integer quantity);
}