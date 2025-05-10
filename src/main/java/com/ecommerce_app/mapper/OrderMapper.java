package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CreateOrderRequest;
import com.ecommerce_app.dto.response.OrderItemResponse;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.entity.Order;
import com.ecommerce_app.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order createOrderRequestToOrder(CreateOrderRequest createOrderRequest);

    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponse orderToOrderResponse(Order order);

    List<OrderResponse> ordersToOrderResponses(List<Order> orders);

    default OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .unitPrice(orderItem.getUnitPrice())
                .quantity(orderItem.getQuantity())
                .subtotal(orderItem.getSubtotal())
                .build();
    }

    default List<OrderItemResponse> orderItemsToOrderItemResponses(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.stream()
                .map(this::orderItemToOrderItemResponse)
                .toList();
    }
}
