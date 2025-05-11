package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ProductImageResponse;
import com.ecommerce_app.entity.ProductImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "variant", ignore = true)
    ProductImage toEntity(ProductImageCreationRequest dto);

    @Mapping(target = "variantId", expression = "java(entity.getVariant() != null ? entity.getVariant().getId() : null)")
    ProductImageResponse toDto(ProductImage entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "variant", ignore = true)
    void updateEntityFromDto(ProductImageUpdateRequest dto, @MappingTarget ProductImage entity);
}
