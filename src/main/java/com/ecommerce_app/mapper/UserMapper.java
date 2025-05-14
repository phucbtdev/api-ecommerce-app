package com.ecommerce_app.mapper;


import com.ecommerce_app.dto.request.UserCreationRequest;
import com.ecommerce_app.dto.request.UserUpdateRequest;
import com.ecommerce_app.dto.response.RoleResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps User entity to UserResponseDto
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "roles", source = "roles")
    UserResponse toResponse(User user);

    /**
     * Maps UserCreationDto to User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "wishlist", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserCreationRequest userCreationDto);

    /**
     * Updates existing User entity with UserUpdateDto data
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "wishlist", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UserUpdateRequest userUpdateDto, @MappingTarget User user);

    /**
     * Maps Role entity to RoleDto
     */
    @Mapping(target = "name", source = "name")
    RoleResponse roleToRoleDto(Role role);

    /**
     * Maps Set of Role entities to Set of RoleDto objects
     */
    Set<RoleResponse> rolesToRoleDtos(Set<Role> roles);
}
