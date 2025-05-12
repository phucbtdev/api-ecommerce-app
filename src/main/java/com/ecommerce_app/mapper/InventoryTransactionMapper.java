package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.InventoryTransactionCreationRequest;
import com.ecommerce_app.dto.request.InventoryTransactionUpdateRequest;
import com.ecommerce_app.dto.response.InventoryTransactionResponse;
import com.ecommerce_app.entity.Inventory;
import com.ecommerce_app.entity.InventoryTransaction;
import com.ecommerce_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryTransactionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "inventory", source = "inventory")
    @Mapping(target = "createdBy", source = "user")
    InventoryTransaction toEntity(InventoryTransactionCreationRequest request, Inventory inventory, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntity(@MappingTarget InventoryTransaction transaction, InventoryTransactionUpdateRequest request);

    @Mapping(target = "inventoryId", source = "inventory.id")
    @Mapping(target = "createdBy", source = "createdBy.id")
    @Mapping(target = "createdByUsername", source = "createdBy.username")
    InventoryTransactionResponse toResponse(InventoryTransaction transaction);
}