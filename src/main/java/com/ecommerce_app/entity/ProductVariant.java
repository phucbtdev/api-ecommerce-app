package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_variants")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariant extends  BaseEntity {

    String sku;

    @Column(nullable = false)
    String name;

    @Column(precision = 10, scale = 2)
    BigDecimal priceDifference;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    Set<VariantAttribute> attributes = new HashSet<>();

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL)
    Set<ProductImage> images = new HashSet<>();

    @OneToOne(mappedBy = "productVariant", cascade = CascadeType.ALL)
    Inventory inventory;
}
