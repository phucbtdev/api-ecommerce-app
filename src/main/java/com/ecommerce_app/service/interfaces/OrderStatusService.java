package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.OrderStatusResponse;

import java.util.List;
import java.util.UUID;

public interface OrderStatusService {
    OrderStatusResponse createOrderStatus(OrderStatusCreationRequest request);
    OrderStatusResponse getOrderStatus(UUID id);
    List<OrderStatusResponse> getAllOrderStatuses();
    OrderStatusResponse updateOrderStatus(UUID id, OrderStatusUpdateRequest request);
    void deleteOrderStatus(UUID id);
}
