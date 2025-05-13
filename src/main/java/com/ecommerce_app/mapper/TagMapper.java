package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.TagCreationRequest;
import com.ecommerce_app.dto.request.TagUpdateRequest;
import com.ecommerce_app.dto.response.TagResponse;
import com.ecommerce_app.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Tag toEntity(TagCreationRequest request);

    TagResponse toResponse(Tag tag);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntity(@MappingTarget Tag tag, TagUpdateRequest request);
}