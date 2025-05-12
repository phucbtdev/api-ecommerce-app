package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import com.ecommerce_app.service.interfaces.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryCreationRequest request) {
        return new ResponseEntity<>(inventoryService.createInventory(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> getInventoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/product-variant/{productVariantId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> getInventoryByProductVariantId(@PathVariable UUID productVariantId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductVariantId(productVariantId));
    }

    @GetMapping("/sku/{sku}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> getInventoryBySku(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getInventoryBySku(sku));
    }

    @GetMapping("/product-variant-sku/{sku}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> getInventoryByProductVariantSku(@PathVariable String sku) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductVariantSku(sku));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<InventoryResponse>> getAllInventories() {
        return ResponseEntity.ok(inventoryService.getAllInventories());
    }

    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<Page<InventoryResponse>> getAllInventoriesPaged(Pageable pageable) {
        return ResponseEntity.ok(inventoryService.getAllInventories(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryUpdateRequest request) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<Void> deleteInventory(@PathVariable UUID id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock-quantity")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER')")
    public ResponseEntity<InventoryResponse> updateStockQuantity(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.updateStockQuantity(id, quantity));
    }

    @PatchMapping("/{id}/reserve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> reserveStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reserveStock(id, quantity));
    }

    @PatchMapping("/{id}/release-reservation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> releaseReservedStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.releaseReservedStock(id, quantity));
    }

    @PatchMapping("/{id}/commit-reservation")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY_MANAGER') or hasRole('STAFF')")
    public ResponseEntity<InventoryResponse> commitReservedStock(
            @PathVariable UUID id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.commitReservedStock(id, quantity));
    }
}
