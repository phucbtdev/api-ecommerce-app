package com.ecommerce_app.mapper;


import com.ecommerce_app.dto.response.UserProfileResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.entity.Role;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
    UserResponse userToUserResponse(User user);

    @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

    UserProfileResponse profileToProfileResponse(UserProfile profile);
}
