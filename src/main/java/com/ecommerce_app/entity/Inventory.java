package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "inventories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Inventory extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariant productVariant;

    @Column(nullable = false)
    Integer stockQuantity;

    Integer reservedQuantity = 0;

    Integer reorderLevel;

    String sku;

    String location;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    Set<InventoryTransaction> transactions = new HashSet<>();
}
