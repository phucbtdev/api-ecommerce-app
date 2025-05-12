package com.ecommerce_app.repository;

import com.ecommerce_app.entity.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    List<InventoryTransaction> findByInventoryId(UUID inventoryId);

    Page<InventoryTransaction> findByInventoryId(UUID inventoryId, Pageable pageable);

    List<InventoryTransaction> findByInventoryIdAndTransactionType(UUID inventoryId, String transactionType);

    List<InventoryTransaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<InventoryTransaction> findByReference(String reference);
}

