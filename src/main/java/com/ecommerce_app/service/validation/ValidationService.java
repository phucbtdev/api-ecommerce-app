package com.ecommerce_app.service.validation;

import com.ecommerce_app.exception.BadRequestException;
import com.ecommerce_app.repository.CategoryRepository;
import com.ecommerce_app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public void validateCategoryNameUnique(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new BadRequestException("Category with name '" + name + "' already exists");
        }
    }

    public void validateCategorySlugUnique(String slug) {
        if (categoryRepository.existsBySlug(slug)) {
            throw new BadRequestException("Category with slug '" + slug + "' already exists");
        }
    }

    public void validateProductSkuUnique(String sku) {
        if (productRepository.existsBySku(sku)) {
            throw new BadRequestException("Product with SKU '" + sku + "' already exists");
        }
    }

    public void validateProductSlugUnique(String slug) {
        if (productRepository.existsBySlug(slug)) {
            throw new BadRequestException("Product with slug '" + slug + "' already exists");
        }
    }
}
