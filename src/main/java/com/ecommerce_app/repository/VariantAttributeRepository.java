package com.ecommerce_app.repository;

import com.ecommerce_app.entity.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, UUID> {
}
