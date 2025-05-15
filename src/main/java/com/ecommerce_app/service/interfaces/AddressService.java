/**
 * Service interface for managing user addresses in the e-commerce application.
 * Provides functionality for creating, retrieving, updating, and deleting address records,
 * as well as managing default addresses and filtering addresses by type.
 */
package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.request.AddressCreationRequest;
import com.ecommerce_app.dto.request.AddressUpdateRequest;
import com.ecommerce_app.dto.response.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {
    /**
     * Creates a new address record for a user.
     *
     * @param addressCreationRequest The DTO containing address creation details
     * @return The created address as an AddressResponse
     */
    AddressResponse createAddress(AddressCreationRequest addressCreationRequest);

    /**
     * Updates an existing address record.
     *
     * @param id The unique identifier of the address to update
     * @param addressUpdateRequest The DTO containing updated address details
     * @return The updated address as an AddressResponse
     */
    AddressResponse updateAddress(UUID id, AddressUpdateRequest addressUpdateRequest);

    /**
     * Retrieves an address by its unique identifier.
     *
     * @param id The unique identifier of the address
     * @return The address as an AddressResponse
     */
    AddressResponse getAddressById(UUID id);

    /**
     * Retrieves all addresses associated with a specific user.
     *
     * @param userId The unique identifier of the user
     * @return A list of addresses as AddressResponse objects
     */
    List<AddressResponse> getAddressesByUserId(UUID userId);

    /**
     * Retrieves all shipping addresses for a specific user.
     *
     * @param userId The unique identifier of the user
     * @return A list of shipping addresses as AddressResponse objects
     */
    List<AddressResponse> getShippingAddressesByUserId(UUID userId);

    /**
     * Retrieves all billing addresses for a specific user.
     *
     * @param userId The unique identifier of the user
     * @return A list of billing addresses as AddressResponse objects
     */
    List<AddressResponse> getBillingAddressesByUserId(UUID userId);

    /**
     * Retrieves the default address for a specific user.
     *
     * @param userId The unique identifier of the user
     * @return The default address as an AddressResponse
     */
    AddressResponse getDefaultAddressByUserId(UUID userId);

    /**
     * Sets a specific address as the default address for its associated user.
     *
     * @param id The unique identifier of the address to set as default
     * @return The updated address as an AddressResponse
     */
    AddressResponse setAddressAsDefault(UUID id);

    /**
     * Deletes an address by its unique identifier.
     *
     * @param id The unique identifier of the address to delete
     */
    void deleteAddress(UUID id);

    /**
     * Checks if a user has a default address set.
     *
     * @param userId The unique identifier of the user
     * @return true if the user has a default address, false otherwise
     */
    boolean hasDefaultAddress(UUID userId);

    /**
     * Validates and retrieves an address based on user, address ID, and address type.
     *
     * @param userId The unique identifier of the user
     * @param addressId The unique identifier of the address
     * @param addressType The type of address (e.g., "SHIPPING", "BILLING")
     * @return The validated address as an AddressResponse
     */
    AddressResponse validateAndGetAddress(UUID userId, UUID addressId, String addressType);
}