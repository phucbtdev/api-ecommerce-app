package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.InventoryResponse;
import com.ecommerce_app.dto.response.ProductVariantResponse;
import com.ecommerce_app.dto.response.VariantAttributeResponse;
import com.ecommerce_app.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ProductImageMapper.class})
public interface ProductVariantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    ProductVariant toEntity(ProductVariantCreationRequest dto);

    @Mapping(target = "attributes", source = "attributes")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "inventory", source = "inventory")
    ProductVariantResponse toDto(ProductVariant entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    void updateEntityFromDto(ProductVariantUpdateRequest dto, @MappingTarget ProductVariant entity);

    @AfterMapping
    default void mapAttributes(@MappingTarget ProductVariant entity, ProductVariantCreationRequest dto) {
        if (dto.getAttributes() != null) {
            entity.getAttributes().clear();
            dto.getAttributes().forEach(attrDto -> {
                VariantAttribute attr = new VariantAttribute();
                attr.setName(attrDto.getName());
                attr.setValue(attrDto.getValue());
                attr.setVariant(entity);
                entity.getAttributes().add(attr);
            });
        }
    }

    @AfterMapping
    default void mapInventory(@MappingTarget ProductVariant entity, ProductVariantCreationRequest dto) {
        if (dto.getInventory() != null) {
            Inventory inventory = new Inventory();
            inventory.setStockQuantity(dto.getInventory().getStockQuantity());
            inventory.setProductVariant(entity);
            entity.setInventory(inventory);
        }
    }

    default VariantAttributeResponse mapVariantAttribute(VariantAttribute attribute) {
        if (attribute == null) {
            return null;
        }
        return VariantAttributeResponse.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .value(attribute.getValue())
                .build();
    }

    default InventoryResponse mapInventory(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return InventoryResponse.builder()
                .id(inventory.getId())
                .stockQuantity(inventory.getStockQuantity())
                .build();
    }
}