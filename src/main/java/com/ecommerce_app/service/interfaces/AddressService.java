package com.ecommerce_app.service.interfaces;


import com.ecommerce_app.dto.request.AddressCreationRequest;
import com.ecommerce_app.dto.request.AddressUpdateRequest;
import com.ecommerce_app.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    AddressResponse createAddress(AddressCreationRequest addressCreationRequest);

    AddressResponse updateAddress(UUID id, AddressUpdateRequest addressUpdateRequest);

    AddressResponse getAddressById(UUID id);

    List<AddressResponse> getAddressesByUserId(UUID userId);

    List<AddressResponse> getShippingAddressesByUserId(UUID userId);

    List<AddressResponse> getBillingAddressesByUserId(UUID userId);

    AddressResponse getDefaultAddressByUserId(UUID userId);

    AddressResponse setAddressAsDefault(UUID id);

    void deleteAddress(UUID id);

    boolean hasDefaultAddress(UUID userId);

    AddressResponse validateAndGetAddress(UUID userId, UUID addressId, String addressType);
}
