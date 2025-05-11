package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.ProductBasicResponse;
import com.ecommerce_app.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductCreationRequest request);

    ProductResponse updateProduct(UUID id, ProductUpdateRequest request);

    ProductResponse getProductById(UUID id);

    ProductResponse getProductBySlug(String slug);

    Page<ProductBasicResponse> getAllProducts(Pageable pageable);

    Page<ProductBasicResponse> getActiveProducts(Pageable pageable);

    Page<ProductBasicResponse> getProductsByCategory(UUID categoryId, Pageable pageable);

    Page<ProductBasicResponse> getProductsByTag(UUID tagId, Pageable pageable);

    Page<ProductBasicResponse> searchProducts(String keyword, Pageable pageable);

    void deleteProduct(UUID id);

    boolean toggleProductActive(UUID id);
}