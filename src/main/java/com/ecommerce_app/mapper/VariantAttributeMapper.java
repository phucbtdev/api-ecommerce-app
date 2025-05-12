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
    VariantAttribute toEntity(VariantAttributeCreationRequest request, ProductVariant variant);

    VariantAttributeResponse toResponse(VariantAttribute entity);

    void updateEntityFromRequest(VariantAttributeUpdateRequest request, @MappingTarget VariantAttribute entity);
}