package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface InventoryService {

    InventoryResponse createInventory(InventoryCreationRequest request);

    InventoryResponse getInventoryById(UUID id);

    InventoryResponse getInventoryByProductVariantId(UUID productVariantId);

    InventoryResponse getInventoryBySku(String sku);

    InventoryResponse getInventoryByProductVariantSku(String sku);

    List<InventoryResponse> getAllInventories();

    Page<InventoryResponse> getAllInventories(Pageable pageable);

    InventoryResponse updateInventory(UUID id, InventoryUpdateRequest request);

    void deleteInventory(UUID id);

    InventoryResponse adjustStockQuantity(UUID id, Integer quantity);

    InventoryResponse updateStockQuantity(UUID id, Integer stockQuantity);

    InventoryResponse reserveStock(UUID id, Integer quantity);

    InventoryResponse releaseReservedStock(UUID id, Integer quantity);

    InventoryResponse commitReservedStock(UUID id, Integer quantity);
}
