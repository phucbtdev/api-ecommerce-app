package com.ecommerce_app.repository;

import com.ecommerce_app.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {

    List<ProductImage> findByProductId(UUID productId);

    List<ProductImage> findByProductIdOrderBySortOrderAsc(UUID productId);

    List<ProductImage> findByVariantId(UUID variantId);

    List<ProductImage> findByVariantIdOrderBySortOrderAsc(UUID variantId);

    Optional<ProductImage> findByProductIdAndIsMainTrue(UUID productId);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :productId AND pi.variant IS NULL")
    List<ProductImage> findProductLevelImages(@Param("productId") UUID productId);

    @Modifying
    @Query("UPDATE ProductImage pi SET pi.isMain = false WHERE pi.product.id = :productId AND pi.id != :imageId")
    void unsetMainImageExcept(@Param("productId") UUID productId, @Param("imageId") UUID imageId);

    void deleteByProductId(UUID productId);

    void deleteByVariantId(UUID variantId);
}