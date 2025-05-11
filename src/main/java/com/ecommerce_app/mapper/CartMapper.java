package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CartCreationRequest;
import com.ecommerce_app.dto.request.CartUpdateRequest;
import com.ecommerce_app.dto.response.CartResponse;
import com.ecommerce_app.entity.Cart;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "couponId", source = "appliedCoupon.id")
    @Mapping(target = "couponCode", source = "appliedCoupon.code")
    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "appliedCoupon.id", source = "couponId")
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Cart toCart(CartCreationRequest cartRequest);

    @Mapping(target = "appliedCoupon.id", source = "couponId")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCartFromDto(CartUpdateRequest cartUpdateRequest, @MappingTarget Cart cart);
}
