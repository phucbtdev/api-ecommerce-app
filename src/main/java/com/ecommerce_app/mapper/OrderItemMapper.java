package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.OrderItemCreationRequest;
import com.ecommerce_app.dto.request.OrderItemUpdateRequest;
import com.ecommerce_app.dto.response.OrderItemResponse;
import com.ecommerce_app.entity.OrderItem;
import com.ecommerce_app.entity.Product;
import com.ecommerce_app.entity.ProductImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "productVariant.id", source = "productVariantId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "variantInfo", source = "variantInfo")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderItem toEntity(OrderItemCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "productVariant.id", source = "productVariantId")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "variantInfo", source = "variantInfo")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(OrderItemUpdateRequest request, @MappingTarget OrderItem orderItem);

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.sku")
    @Mapping(target = "productImageUrl", source = "product", qualifiedByName = "getMainProductImageUrl")
    @Mapping(target = "productVariantId", source = "productVariant.id")
    @Mapping(target = "variantName", source = "productVariant.name")
    OrderItemResponse toResponse(OrderItem orderItem);

    @AfterMapping
    default void mapMissingValues(@MappingTarget OrderItemResponse response, OrderItem orderItem) {
        // Handle null productVariant
        if (orderItem.getProductVariant() == null) {
            response.setProductVariantId(null);
            response.setVariantName(null);
        }
    }

    @Named("getMainProductImageUrl")
    default String getMainProductImageUrl(Product product) {
        if (product == null || product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }

        return product.getImages().stream()
                .filter(ProductImage::getIsMain)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }
}