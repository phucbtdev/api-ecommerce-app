package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.ProductImageCreationRequest;
import com.ecommerce_app.dto.request.ProductImageUpdateRequest;
import com.ecommerce_app.dto.response.ProductImageResponse;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {

    ProductImageResponse createProductImage(UUID productId, ProductImageCreationRequest request);

    ProductImageResponse updateProductImage(UUID id, ProductImageUpdateRequest request);

    ProductImageResponse getProductImageById(UUID id);

    List<ProductImageResponse> getImagesByProductId(UUID productId);

    List<ProductImageResponse> getImagesByVariantId(UUID variantId);

    List<ProductImageResponse> getProductLevelImages(UUID productId);

    void deleteProductImage(UUID id);

    ProductImageResponse setMainImage(UUID imageId);
}
