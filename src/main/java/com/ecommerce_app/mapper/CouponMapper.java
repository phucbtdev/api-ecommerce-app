package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CouponCreationRequest;
import com.ecommerce_app.dto.request.CouponUpdateRequest;
import com.ecommerce_app.dto.response.CategoryResponse;
import com.ecommerce_app.dto.response.CouponResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import com.ecommerce_app.entity.Category;
import com.ecommerce_app.entity.Coupon;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.repository.CategoryRepository;
import com.ecommerce_app.repository.ProductRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, ProductMapper.class})
public abstract class CouponMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "usedCount", constant = "0")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "applicableCategories", source = "applicableCategoryIds", qualifiedByName = "categoryIdsToCategories")
    @Mapping(target = "applicableProducts", source = "applicableProductIds", qualifiedByName = "productIdsToProducts")
    public abstract Coupon toEntity(CouponCreationRequest request);

    @Mapping(target = "applicableCategories", source = "applicableCategories", qualifiedByName = "categoriesToCategoryResponses")
    @Mapping(target = "applicableProducts", source = "applicableProducts", qualifiedByName = "productsToProductResponses")
    public abstract CouponResponse toResponse(Coupon coupon);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "usedCount", ignore = true)
    @Mapping(target = "applicableCategories", source = "applicableCategoryIds", qualifiedByName = "categoryIdsToCategories")
    @Mapping(target = "applicableProducts", source = "applicableProductIds", qualifiedByName = "productIdsToProducts")
    public abstract void updateEntityFromDto(CouponUpdateRequest request, @MappingTarget Coupon coupon);

    @Named("categoryIdsToCategories")
    protected Set<Category> categoryIdsToCategories(Set<UUID> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        return categoryIds.stream()
                .map(id -> categoryRepository.findById(id).orElse(null))
                .filter(category -> category != null)
                .collect(Collectors.toSet());
    }

    @Named("productIdsToProducts")
    protected Set<Product> productIdsToProducts(Set<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return new HashSet<>();
        }

        return productIds.stream()
                .map(id -> productRepository.findById(id).orElse(null))
                .filter(product -> product != null)
                .collect(Collectors.toSet());
    }

    @Named("categoriesToCategoryResponses")
    protected Set<CategoryResponse> categoriesToCategoryResponses(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return new HashSet<>();
        }

        return categories.stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toSet());
    }

    @Named("productsToProductResponses")
    protected Set<ProductResponse> productsToProductResponses(Set<Product> products) {
        if (products == null || products.isEmpty()) {
            return new HashSet<>();
        }

        return products.stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toSet());
    }
}