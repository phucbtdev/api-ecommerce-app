package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.ProductVariantCreationRequest;
import com.ecommerce_app.dto.request.ProductVariantUpdateRequest;
import com.ecommerce_app.dto.response.ProductVariantResponse;

import java.util.List;
import java.util.UUID;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(UUID productId, ProductVariantCreationRequest request);

    ProductVariantResponse updateProductVariant(UUID id, ProductVariantUpdateRequest request);

    ProductVariantResponse getProductVariantById(UUID id);

    List<ProductVariantResponse> getVariantsByProductId(UUID productId);

    void deleteProductVariant(UUID id);

    List<ProductVariantResponse> getVariantsByProductIdAndAttribute(
            UUID productId, String attributeName, String attributeValue);
}
