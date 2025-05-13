package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.VariantAttributeResponse;
import com.ecommerce_app.entity.ProductVariant;
import com.ecommerce_app.entity.VariantAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VariantAttributeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variant", source = "variant")
    @Mapping(target = "name", source = "request.name")
    VariantAttribute toEntity(VariantAttributeCreationRequest request, ProductVariant variant);

    @Mapping(target = "variantId", source = "variant.id")
    VariantAttributeResponse toResponse(VariantAttribute entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "variant", ignore = true)
    void updateEntityFromRequest(VariantAttributeUpdateRequest request, @MappingTarget VariantAttribute entity);
}