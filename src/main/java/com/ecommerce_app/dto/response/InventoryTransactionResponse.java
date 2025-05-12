package com.ecommerce_app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InventoryTransactionResponse {
    private Long id;
    private UUID inventoryId;
    private String transactionType;
    private Integer quantity;
    private String reference;
    private String notes;
    private UUID createdBy;
    private String createdByUsername;
    private LocalDateTime createdAt;
}
