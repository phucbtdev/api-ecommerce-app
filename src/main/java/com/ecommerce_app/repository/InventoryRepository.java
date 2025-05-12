package com.ecommerce_app.repository;

import com.ecommerce_app.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findByProductVariantId(UUID productVariantId);

    Optional<Inventory> findBySku(String sku);

    @Query("SELECT i FROM Inventory i WHERE i.productVariant.sku = :sku")
    Optional<Inventory> findByProductVariantSku(@Param("sku") String sku);

    @Query("SELECT COUNT(i) > 0 FROM Inventory i WHERE i.productVariant.id = :productVariantId")
    boolean existsByProductVariantId(@Param("productVariantId") UUID productVariantId);
}
