package com.ecommerce_app.repository;

import com.ecommerce_app.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {

    List<ProductVariant> findByProductId(UUID productId);

    Optional<ProductVariant> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT pv FROM ProductVariant pv JOIN pv.attributes attr WHERE " +
            "pv.product.id = :productId AND attr.name = :attrName AND attr.value = :attrValue")
    List<ProductVariant> findByProductIdAndAttribute(
            @Param("productId") UUID productId,
            @Param("attrName") String attributeName,
            @Param("attrValue") String attributeValue);

    void deleteByProductId(UUID productId);
}