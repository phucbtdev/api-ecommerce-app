package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.InventoryTransactionCreationRequest;
import com.ecommerce_app.dto.request.InventoryTransactionUpdateRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.InventoryTransactionResponse;
import com.ecommerce_app.service.interfaces.InventoryTransactionService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing inventory transactions.
 * Provides endpoints for creating, retrieving, updating, and deleting inventory transaction records,
 * as well as querying transactions by various criteria.
 */
@RestController
@RequestMapping("/inventory-transactions")
@RequiredArgsConstructor
@Tag(name = "Inventory Transactions", description = "Inventory transaction management API")
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    /**
     * Creates a new inventory transaction.
     *
     * @param request The transaction creation request
     * @return The created transaction information
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Create a new inventory transaction",
            description = "Creates a new inventory transaction with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to create transaction")
    })
    public ApiResult<InventoryTransactionResponse> createTransaction(
            @Parameter(description = "Transaction creation details", required = true)
            @Valid @RequestBody InventoryTransactionCreationRequest request) {
        return ApiResult.success("Transaction created successfully",
                transactionService.createTransaction(request));
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction to retrieve
     * @return The transaction information
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transaction by ID",
            description = "Retrieves transaction information for the specified ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transaction")
    })
    public ApiResult<InventoryTransactionResponse> getTransactionById(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        return ApiResult.success("Transaction retrieved successfully",
                transactionService.getTransactionById(id));
    }

    /**
     * Retrieves all transactions for a specific inventory.
     *
     * @param inventoryId The UUID of the inventory
     * @return List of transactions for the specified inventory
     */
    @GetMapping("/inventory/{inventoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transactions by inventory ID",
            description = "Retrieves all transactions for a specific inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transactions")
    })
    public ApiResult<List<InventoryTransactionResponse>> getTransactionsByInventoryId(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID inventoryId) {
        return ApiResult.success("Inventory transactions retrieved successfully",
                transactionService.getTransactionsByInventoryId(inventoryId));
    }

    /**
     * Retrieves transactions for a specific inventory with pagination.
     *
     * @param inventoryId The UUID of the inventory
     * @param pageable Pagination information
     * @return Page of transactions for the specified inventory
     */
    @GetMapping("/inventory/{inventoryId}/paged")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transactions by inventory ID with pagination",
            description = "Retrieves all transactions for a specific inventory with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transactions")
    })
    public ApiResult<Page<InventoryTransactionResponse>> getTransactionsByInventoryIdPaged(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID inventoryId,
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return ApiResult.success("Paged inventory transactions retrieved successfully",
                transactionService.getTransactionsByInventoryId(inventoryId, pageable));
    }

    /**
     * Retrieves transactions by inventory ID and transaction type.
     *
     * @param inventoryId The UUID of the inventory
     * @param transactionType The type of transaction
     * @return List of transactions matching the criteria
     */
    @GetMapping("/inventory/{inventoryId}/type/{transactionType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transactions by type",
            description = "Retrieves transactions for a specific inventory filtered by transaction type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found or no transactions of specified type",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transactions")
    })
    public ApiResult<List<InventoryTransactionResponse>> getTransactionsByType(
            @Parameter(description = "Inventory ID", required = true)
            @PathVariable UUID inventoryId,
            @Parameter(description = "Transaction type", required = true)
            @PathVariable String transactionType) {
        return ApiResult.success("Typed inventory transactions retrieved successfully",
                transactionService.getTransactionsByType(inventoryId, transactionType));
    }

    /**
     * Retrieves transactions within a specified date range.
     *
     * @param start The start date and time
     * @param end The end date and time
     * @return List of transactions within the date range
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transactions by date range",
            description = "Retrieves all transactions within a specified date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date range",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transactions")
    })
    public ApiResult<List<InventoryTransactionResponse>> getTransactionsByDateRange(
            @Parameter(description = "Start date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date and time (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ApiResult.success("Date range transactions retrieved successfully",
                transactionService.getTransactionsByDateRange(start, end));
    }

    /**
     * Retrieves transactions by reference.
     *
     * @param reference The reference string
     * @return List of transactions with the specified reference
     */
    @GetMapping("/reference/{reference}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    @Operation(summary = "Get transactions by reference",
            description = "Retrieves all transactions with the specified reference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "No transactions found with the reference",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to access transactions")
    })
    public ApiResult<List<InventoryTransactionResponse>> getTransactionsByReference(
            @Parameter(description = "Reference string", required = true)
            @PathVariable String reference) {
        return ApiResult.success("Referenced transactions retrieved successfully",
                transactionService.getTransactionsByReference(reference));
    }

    /**
     * Updates an existing transaction.
     *
     * @param id The ID of the transaction to update
     * @param request The update request containing the new information
     * @return The updated transaction information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Update transaction", description = "Updates an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction updated successfully",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to update transaction")
    })
    public ApiResult<InventoryTransactionResponse> updateTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Transaction update details", required = true)
            @Valid @RequestBody InventoryTransactionUpdateRequest request) {
        return ApiResult.success("Transaction updated successfully",
                transactionService.updateTransaction(id, request));
    }

    /**
     * Deletes a transaction.
     *
     * @param id The ID of the transaction to delete
     * @return Empty response with no content status
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    @Operation(summary = "Delete transaction", description = "Deletes an existing transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to delete transaction")
    })
    public ApiResult<Void> deleteTransaction(
            @Parameter(description = "Transaction ID", required = true)
            @PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ApiResult.success("Transaction deleted successfully", null);
    }
}