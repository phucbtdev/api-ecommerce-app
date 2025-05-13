package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.entity.*;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {ProductVariantMapper.class, ProductImageMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Product toEntity(ProductCreationRequest dto);

    @Mapping(target = "categories", source = "categories")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "variants", source = "variants")
    @Mapping(source = "id", target = "id")
    ProductResponse toResponse(Product entity);

    @Mapping(target = "mainImageUrl", expression = "java(getMainImageUrl(entity))")
    ProductBasicResponse toBasicDto(Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "slug", ignore = true, conditionExpression = "java(dto.getSlug() == null)")
    void updateEntityFromDto(ProductUpdateRequest dto, @MappingTarget Product entity);

    @AfterMapping
    default void handleCategoryIds(@MappingTarget Product entity, ProductCreationRequest dto, @Context Set<Category> categories) {
        entity.getCategories().clear();
        entity.getCategories().addAll(categories);
    }

    @AfterMapping
    default void handleTagIds(@MappingTarget Product entity, ProductCreationRequest dto, @Context Set<Tag> tags) {
        entity.getTags().clear();
        entity.getTags().addAll(tags);
    }

    default String getMainImageUrl(Product entity) {
        return entity.getImages().stream()
                .filter(ProductImage::getIsMain)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(entity.getImages().stream()
                        .findFirst()

                        .map(ProductImage::getImageUrl)
                        .orElse(null));
    }

    default CategoryResponse mapCategory(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

}
