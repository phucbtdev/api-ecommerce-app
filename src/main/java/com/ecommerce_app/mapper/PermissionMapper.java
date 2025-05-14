package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.PermissionCreationRequestDto;
import com.ecommerce_app.dto.request.PermissionUpdateRequestDto;
import com.ecommerce_app.dto.response.PermissionResponse;
import com.ecommerce_app.entity.Permission;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     * Maps Permission entity to PermissionResponse
     */
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    PermissionResponse toResponseDto(Permission permission);

    /**
     * Maps PermissionCreationRequestDto to Permission entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Permission toEntity(PermissionCreationRequestDto permissionCreationRequestDto);

    /**
     * Updates existing Permission entity with PermissionUpdateRequestDto data
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntityFromDto(PermissionUpdateRequestDto permissionUpdateRequestDto, @MappingTarget Permission permission);
}
