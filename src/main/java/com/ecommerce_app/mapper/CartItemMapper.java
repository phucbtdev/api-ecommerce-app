package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.CartItemCreationRequest;
import com.ecommerce_app.dto.request.CartItemUpdateRequest;
import com.ecommerce_app.dto.response.CartItemResponse;
import com.ecommerce_app.entity.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartId", source = "cart.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", expression = "java(getFirstProductImage(cartItem))")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "variantName", source = "productVariant.name")
    @Mapping(target = "totalPrice", expression = "java(cartItem.getPrice().multiply(new java.math.BigDecimal(cartItem.getQuantity())))")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "productVariant.id", source = "productVariantId")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CartItem toCartItem(CartItemCreationRequest cartItemRequest);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "productVariant", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCartItemFromDto(CartItemUpdateRequest updateRequest, @MappingTarget CartItem cartItem);

    default String getFirstProductImage(CartItem cartItem) {
        if (cartItem.getProduct() != null &&
                cartItem.getProduct().getImages() != null &&
                !cartItem.getProduct().getImages().isEmpty()) {
            return cartItem.getProduct().getImages().iterator().next().getImageUrl();
        }
        return null;
    }
}
