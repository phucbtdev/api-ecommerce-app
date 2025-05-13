package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.InventoryCreationRequest;
import com.ecommerce_app.dto.request.InventoryUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import com.ecommerce_app.entity.Inventory;
import com.ecommerce_app.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "productVariant", source = "productVariant")
    @Mapping(target = "stockQuantity", source = "request.stockQuantity")
    @Mapping(target = "reservedQuantity", source = "request.reservedQuantity")
    @Mapping(target = "reorderLevel", source = "request.reorderLevel")
    @Mapping(target = "sku", source = "request.sku") // chỉ rõ dùng sku từ request
    @Mapping(target = "location", source = "request.location")
    Inventory toEntity(InventoryCreationRequest request, ProductVariant productVariant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    void updateEntity(@MappingTarget Inventory inventory, InventoryUpdateRequest request);

    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "productVariantName", source = "productVariant.name")
    @Mapping(target = "availableQuantity", expression = "java(inventory.getStockQuantity() - inventory.getReservedQuantity())")
    InventoryResponse toResponse(Inventory inventory);
}

