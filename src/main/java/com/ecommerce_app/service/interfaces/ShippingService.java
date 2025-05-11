package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.ShippingCreationRequest;
import com.ecommerce_app.dto.request.ShippingUpdateRequest;
import com.ecommerce_app.dto.response.ShippingResponse;

import java.util.List;

public interface ShippingService {
    ShippingResponse createShipping(ShippingCreationRequest shippingCreationRequest);

    ShippingResponse updateShipping(Long id, ShippingUpdateRequest shippingUpdateRequest);

    ShippingResponse getShippingById(Long id);

    ShippingResponse getShippingByOrderId(Long orderId);

    List<ShippingResponse> getAllShippings();

    void deleteShipping(Long id);

    boolean existsByOrderId(Long orderId);

    ShippingResponse updateShippingStatus(Long id, String status);

    ShippingResponse updateTrackingInfo(Long id, String trackingNumber, String carrier);

    ShippingResponse updateDeliveryDate(Long id, boolean isActual);
}
