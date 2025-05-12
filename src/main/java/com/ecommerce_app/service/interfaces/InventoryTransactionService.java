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

    InventoryTransactionResponse createTransaction(InventoryTransactionCreationRequest request);

    InventoryTransactionResponse getTransactionById(Long id);

    List<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId);

    Page<InventoryTransactionResponse> getTransactionsByInventoryId(UUID inventoryId, Pageable pageable);

    List<InventoryTransactionResponse> getTransactionsByType(UUID inventoryId, String transactionType);

    List<InventoryTransactionResponse> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end);

    List<InventoryTransactionResponse> getTransactionsByReference(String reference);

    InventoryTransactionResponse updateTransaction(Long id, InventoryTransactionUpdateRequest request);

    void deleteTransaction(Long id);
}

