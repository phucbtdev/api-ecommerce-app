package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.NotificationCreationRequest;
import com.ecommerce_app.dto.request.NotificationUpdateRequest;
import com.ecommerce_app.dto.response.NotificationResponse;
import com.ecommerce_app.entity.Notification;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    Notification toEntity(NotificationCreationRequest request);

    @Mapping(target = "userId", source = "user.id")
    NotificationResponse toResponse(Notification entity);

    List<NotificationResponse> toResponseList(List<Notification> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(NotificationUpdateRequest request, @MappingTarget Notification entity);
}
