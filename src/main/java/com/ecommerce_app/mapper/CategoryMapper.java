package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.request.CategoryUpdateRequest;
import com.ecommerce_app.dto.response.CategoryBasicResponse;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CategoryTreeResponse;
import com.ecommerce_app.entity.Category;
import org.mapstruct.*;

import java.util.Set;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryCreationRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "subcategories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryUpdateRequest dto, @MappingTarget Category entity);

    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "subcategories", source = "subcategories")
    @Mapping(target = "productCount", ignore = true)
    CategoryResponse toResponse(Category entity);

    CategoryBasicResponse toBasicResponseDto(Category entity);

    @Mapping(target = "subcategories", source = "subcategories")
    CategoryTreeResponse toTreeResponseDto(Category entity);

    Set<CategoryBasicResponse> toBasicResponseDtoSet(Set<Category> categories);

    Set<CategoryTreeResponse> toTreeResponseDtoSet(Set<Category> categories);
}