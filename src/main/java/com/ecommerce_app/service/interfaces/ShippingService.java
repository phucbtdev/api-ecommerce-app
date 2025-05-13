package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ShippingResponse;

import java.util.List;
import java.util.UUID;

public interface ShippingService {
    ShippingResponse createShipping(ShippingCreationRequest shippingCreationRequest);

    ShippingResponse updateShipping(UUID id, ShippingUpdateRequest shippingUpdateRequest);

    ShippingResponse getShippingById(UUID id);

    ShippingResponse getShippingByOrderId(UUID orderId);

    List<ShippingResponse> getAllShippings();

    void deleteShipping(UUID id);

    boolean existsByOrderId(UUID orderId);

    ShippingResponse updateShippingStatus(UUID id, String status);

    ShippingResponse updateTrackingInfo(UUID id, String trackingNumber, String carrier);

    ShippingResponse updateDeliveryDate(UUID id, boolean isActual);
}
