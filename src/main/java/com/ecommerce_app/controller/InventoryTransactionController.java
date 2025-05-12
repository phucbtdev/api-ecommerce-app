package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.InventoryTransactionCreationRequest;
import com.ecommerce_app.dto.request.InventoryTransactionUpdateRequest;
import com.ecommerce_app.dto.response.InventoryTransactionResponse;
import com.ecommerce_app.service.interfaces.InventoryTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {

    private final InventoryTransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryTransactionResponse> createTransaction(
            @Valid @RequestBody InventoryTransactionCreationRequest request) {
        return new ResponseEntity<>(transactionService.createTransaction(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryTransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/inventory/{inventoryId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<InventoryTransactionResponse>> getTransactionsByInventoryId(
            @PathVariable UUID inventoryId) {
        return ResponseEntity.ok(transactionService.getTransactionsByInventoryId(inventoryId));
    }

    @GetMapping("/inventory/{inventoryId}/paged")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<Page<InventoryTransactionResponse>> getTransactionsByInventoryIdPaged(
            @PathVariable UUID inventoryId,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactionsByInventoryId(inventoryId, pageable));
    }

    @GetMapping("/inventory/{inventoryId}/type/{transactionType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<InventoryTransactionResponse>> getTransactionsByType(
            @PathVariable UUID inventoryId,
            @PathVariable String transactionType) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(inventoryId, transactionType));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<InventoryTransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getTransactionsByDateRange(start, end));
    }

    @GetMapping("/reference/{reference}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<InventoryTransactionResponse>> getTransactionsByReference(
            @PathVariable String reference) {
        return ResponseEntity.ok(transactionService.getTransactionsByReference(reference));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryTransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody InventoryTransactionUpdateRequest request) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
