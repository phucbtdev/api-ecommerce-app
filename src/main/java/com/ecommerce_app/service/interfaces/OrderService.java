package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.CreateOrderRequest;
import com.ecommerce_app.dto.request.UpdateOrderStatusRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.dto.response.PagedResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.OrderStatus;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest createOrderRequest);

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByOrderNumber(String orderNumber);

    PagedResponse<OrderResponse> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    PagedResponse<OrderResponse> getOrdersByStatus(OrderStatus status, int pageNo, int pageSize);

    PagedResponse<OrderResponse> getOrdersByCustomerEmail(String customerEmail, int pageNo, int pageSize);

    OrderResponse updateOrderStatus(Long id, UpdateOrderStatusRequest updateOrderStatusRequest);

    void deleteOrder(Long id);
}
