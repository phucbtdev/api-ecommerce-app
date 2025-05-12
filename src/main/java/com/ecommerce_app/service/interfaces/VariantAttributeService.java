package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.VariantAttributeCreationRequest;
import com.ecommerce_app.dto.request.VariantAttributeUpdateRequest;
import com.ecommerce_app.dto.response.VariantAttributeResponse;

import java.util.UUID;

public interface VariantAttributeService {
    VariantAttributeResponse create(VariantAttributeCreationRequest request);

    VariantAttributeResponse findById(UUID id);

    VariantAttributeResponse update(UUID id, VariantAttributeUpdateRequest request);

    void delete(UUID id);
}
