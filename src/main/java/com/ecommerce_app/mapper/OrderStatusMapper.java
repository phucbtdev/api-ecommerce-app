package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.OrderStatusCreationRequest;
import com.ecommerce_app.dto.request.OrderStatusUpdateRequest;
import com.ecommerce_app.dto.response.OrderStatusResponse;
import com.ecommerce_app.entity.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderStatusMapper {
    OrderStatus toEntity(OrderStatusCreationRequest request);

    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().getTime() : null)")
    @Mapping(target = "updatedAt", expression = "java(entity.getUpdatedAt() != null ? entity.getUpdatedAt().getTime() : null)")
    OrderStatusResponse toResponse(OrderStatus entity);

    void updateEntityFromRequest(OrderStatusUpdateRequest request, @MappingTarget OrderStatus entity);
}
