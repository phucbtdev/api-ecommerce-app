package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CategoryCreationRequest;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.entity.Category;
import com.ecommerce_app.mapper.config.MapperConfiguration;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface CategoryMapper {

    @Mapping(target = "productCount", ignore = true)
    CategoryResponse toEntity(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    @Mapping(target = "products", ignore = true)
    Category toEntity(CategoryCreationRequest categoryCreationRequest);

    @AfterMapping
    default void mapProductCount(@MappingTarget CategoryCreationRequest categoryCreationRequest, Category category) {
        if (category.getProducts() != null) {
            categoryCreationRequest.setProductCount((long) category.getProducts().size());
        } else {
            categoryCreationRequest.setProductCount(0L);
        }
    }
}
