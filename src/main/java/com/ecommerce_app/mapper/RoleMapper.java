package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.RoleCreationRequest;
import com.ecommerce_app.dto.request.RoleUpdateRequest;
import com.ecommerce_app.dto.response.BasicRoleResponse;
import com.ecommerce_app.dto.response.RoleResponse;
import com.ecommerce_app.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {PermissionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Role toEntity(RoleCreationRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    void updateEntityFromDto(RoleUpdateRequest dto, @MappingTarget Role entity);

    RoleResponse toResponseDto(Role entity);

    BasicRoleResponse toBasicResponseDto(Role entity);
}
