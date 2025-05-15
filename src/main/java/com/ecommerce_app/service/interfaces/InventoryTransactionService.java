/**
 * Service interface for managing inventory transactions in the e-commerce application.
 * Provides functionality for tracking and managing all changes to inventory,
 * including adjustments, reservations, and commitments.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.InventoryTransactionCreationRequest;
import com.ecommerce_app.dto.request.InventoryTransactionUpdateRequest;
import com.ecommerce_app.dto.response.InventoryTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface InventoryTransactionService {
    /**
     * Creates a new inventory transaction record.
     *
     * @param request The DTO containing transaction creation details
     * @return The created transaction as an InventoryTransactionResponse
     */
    InventoryTransactionResponse createTransaction(InventoryTransactionCreationRequest request);

    /**
     * Retrieves a transaction record by its unique identifier.
     *
     * @param id The unique identifier of the transaction
     * @return The transaction as an InventoryTransactionResponse
     */
    InventoryTransactionResponse getTransactionById(Long id);

    /**
     * Retrieves all transactions for a specific inventory item.
     *
     * @param inventoryId The unique identifier of the inventory record
     * @return A list of transactions as InventoryTransactionResponse objects
     */
    List<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId);

    /**
     * Retrieves all transactions for a specific inventory item with pagination support.
     *
     * @param inventoryId The unique identifier of the inventory record
     * @param pageable The pagination information
     * @return A page of transactions as InventoryTransactionResponse objects
     */
    Page<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId, Pageable pageable);

    /**
     * Retrieves transactions of a specific type for an inventory item.
     *
     * @param inventoryId The unique identifier of the inventory record
     * @param transactionType The type of transaction (e.g., "ADJUSTMENT", "RESERVATION", "COMMIT")
     * @return A list of transactions as InventoryTransactionResponse objects
     */
    List<InventoryTransactionResponse> getTransactionsByType(UUID inventoryId, String transactionType);

    /**
     * Retrieves transactions that occurred within a specific date range.
     *
     * @param start The start date and time
     * @param end The end date and time
     * @return A list of transactions as InventoryTransactionResponse objects
     */
    List<InventoryTransactionResponse> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Retrieves transactions associated with a specific reference (e.g., order ID).
     *
     * @param reference The reference identifier
     * @return A list of transactions as InventoryTransactionResponse objects
     */
    List<InventoryTransactionResponse> getTransactionsByReference(String reference);

    /**
     * Updates an existing inventory transaction record.
     *
     * @param id The unique identifier of the transaction to update
     * @param request The DTO containing updated transaction details
     * @return The updated transaction as an InventoryTransactionResponse
     */
    InventoryTransactionResponse updateTransaction(Long id, InventoryTransactionUpdateRequest request);

    /**
     * Deletes an inventory transaction record by its unique identifier.
     *
     * @param id The unique identifier of the transaction to delete
     */
    void deleteTransaction(Long id);
}