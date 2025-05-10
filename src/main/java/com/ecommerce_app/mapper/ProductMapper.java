package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.mapper.config.MapperConfiguration;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface ProductMapper {

    ProductResponse productToProductResponse(Product product);

    List<ProductResponse> productsToProductResponses(List<Product> products);

    @Mapping(target = "category.id", source = "categoryId")
    Product toEntity(ProductCreationRequest productCreationRequest);

    @AfterMapping
    default void setCategoryNull(@MappingTarget Product product) {
        if (product.getCategory() != null && product.getCategory().getId() == null) {
            product.setCategory(null);
        }
    }
}
