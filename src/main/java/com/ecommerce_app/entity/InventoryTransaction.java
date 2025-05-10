package com.ecommerce_app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "inventory_id", nullable = false)
    Inventory inventory;

    @Column(nullable = false)
    String transactionType; // STOCK_IN, STOCK_OUT, ADJUSTMENT, RESERVATION, RELEASE_RESERVATION

    @Column(nullable = false)
    Integer quantity;

    String reference; // Order ID, Purchase Order ID, etc.

    String notes;

    @ManyToOne
    @JoinColumn(name = "created_by")
    User createdBy;

    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
