package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ProductCreationRequest;
import com.ecommerce_app.dto.request.ProductUpdateRequest;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.dto.response.ProductResponse;

import java.math.BigDecimal;

public interface ProductService {

    PagedResponse<ProductResponse> getAllProducts(int page, int size, String sortBy, String sortDir);

    PagedResponse<ProductResponse> searchProducts(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean isActive,
            int page,
            int size);

    PagedResponse<ProductResponse> getProductsByCategoryId(Long categoryId, int page, int size);

    ProductResponse getProductById(Long id);

    ProductResponse getProductBySlug(String slug);

    ProductResponse getProductBySku(String sku);

    ProductResponse createProduct(ProductCreationRequest productCreationRequest);

    ProductResponse updateProduct(Long id, ProductUpdateRequest productUpdateRequest);

    void deleteProduct(Long id);

    ProductResponse toggleProductStatus(Long id);

    ProductResponse updateProductStock(Long id, Integer quantity);
}
