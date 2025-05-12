package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.UserActivityCreationRequest;
import com.ecommerce_app.dto.request.UserActivityUpdateRequest;
import com.ecommerce_app.dto.response.UserActivityResponse;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.UserActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserActivityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    UserActivity toEntity(UserActivityCreationRequest request, User user);

    @Mapping(target = "userId", source = "user.id")
    UserActivityResponse toResponse(UserActivity userActivity);

    void updateEntity(UserActivityUpdateRequest request, @MappingTarget UserActivity userActivity);
}
