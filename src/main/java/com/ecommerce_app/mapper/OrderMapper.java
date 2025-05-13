package com.ecommerce_app.mapper;
import com.ecommerce_app.dto.request.OrderCreationRequest;
import com.ecommerce_app.dto.request.OrderUpdateRequest;
import com.ecommerce_app.dto.response.OrderResponse;
import com.ecommerce_app.entity.*;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring",
        uses = {OrderItemMapper.class, AddressMapper.class, PaymentMapper.class, ShippingMapper.class, CouponMapper.class},
        imports = {java.util.UUID.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", expression = "java(generateOrderNumber())")
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "shippingAmount", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "customerNotes", source = "customerNotes")
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "shipping", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "billingAddress.id", source = "billingAddressId")
    @Mapping(target = "shippingAddress.id", source = "shippingAddressId")
    @Mapping(target = "coupon.id", source = "couponId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "shippingAmount", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "shipping", ignore = true)
    @Mapping(target = "status.id", source = "statusId")
    @Mapping(target = "billingAddress.id", source = "billingAddressId")
    @Mapping(target = "shippingAddress.id", source = "shippingAddressId")
    @Mapping(target = "coupon", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(OrderUpdateRequest request, @MappingTarget Order order);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userName", expression = "java(order.getUser().getFirstName() + \" \" + order.getUser().getLastName())")
    @Mapping(target = "orderStatusId", source = "status.id")
    @Mapping(target = "orderStatus", source = "status.name")
    OrderResponse toResponse(Order order);

    default String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}