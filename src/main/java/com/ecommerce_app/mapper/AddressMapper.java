package com.ecommerce_app.mapper;

import com.ecommerce_app.dto.request.AddressCreationRequest;
import com.ecommerce_app.dto.request.AddressUpdateRequest;
import com.ecommerce_app.dto.response.AddressResponse;
import com.ecommerce_app.entity.Address;
import com.ecommerce_app.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "billingOrders", ignore = true)
    @Mapping(target = "shippingOrders", ignore = true)
    @Mapping(target = "phoneNumber", source = "dto.phoneNumber")
    @Mapping(target = "fullName", source = "dto.fullName")
    Address toEntity(AddressCreationRequest dto, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "billingOrders", ignore = true)
    @Mapping(target = "shippingOrders", ignore = true)
    void updateAddressFromDto(AddressUpdateRequest dto, @MappingTarget Address address);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    AddressResponse toDto(Address address);
}
