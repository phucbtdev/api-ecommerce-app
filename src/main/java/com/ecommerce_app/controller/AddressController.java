package com.ecommerce_app.controller;

import com.ecommerce_app.dto.request.AddressCreationRequest;
import com.ecommerce_app.dto.request.AddressUpdateRequest;
import com.ecommerce_app.dto.response.AddressResponse;
import com.ecommerce_app.service.interfaces.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing user addresses.
 * Provides endpoints for creating, updating, retrieving, and deleting addresses,
 * as well as managing default, shipping, and billing addresses for a user.
 */
@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {

    private final AddressService addressService;

    /**
     * Creates a new address.
     *
     * @param addressCreationRequest the request containing details of the address to be created
     * @return the created address
     */
    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody @Valid AddressCreationRequest addressCreationRequest) {
        return new ResponseEntity<>(addressService.createAddress(addressCreationRequest), HttpStatus.CREATED);
    }

    /**
     * Updates an existing address.
     *
     * @param id the ID of the address to be updated
     * @param addressUpdateRequest the request containing updated details of the address
     * @return the updated address
     */
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable UUID id,
                                                         @RequestBody @Valid AddressUpdateRequest addressUpdateRequest) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressUpdateRequest));
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param id the ID of the address to retrieve
     * @return the retrieved address
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    /**
     * Retrieves all addresses associated with a user.
     *
     * @param userId the ID of the user whose addresses are to be retrieved
     * @return a list of addresses
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    /**
     * Retrieves all shipping addresses associated with a user.
     *
     * @param userId the ID of the user whose shipping addresses are to be retrieved
     * @return a list of shipping addresses
     */
    @GetMapping("/user/{userId}/shipping")
    public ResponseEntity<List<AddressResponse>> getShippingAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getShippingAddressesByUserId(userId));
    }

    /**
     * Retrieves all billing addresses associated with a user.
     *
     * @param userId the ID of the user whose billing addresses are to be retrieved
     * @return a list of billing addresses
     */
    @GetMapping("/user/{userId}/billing")
    public ResponseEntity<List<AddressResponse>> getBillingAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getBillingAddressesByUserId(userId));
    }

    /**
     * Retrieves the default address associated with a user.
     *
     * @param userId the ID of the user whose default address is to be retrieved
     * @return the default address
     */
    @GetMapping("/user/{userId}/default")
    public ResponseEntity<AddressResponse> getDefaultAddressByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getDefaultAddressByUserId(userId));
    }

    /**
     * Sets an address as the default address.
     *
     * @param id the ID of the address to set as default
     * @return the updated address marked as default
     */
    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressResponse> setAddressAsDefault(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.setAddressAsDefault(id));
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id the ID of the address to delete
     * @return a response with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if a user has a default address.
     *
     * @param userId the ID of the user to check
     * @return true if the user has a default address, false otherwise
     */
    @GetMapping("/user/{userId}/has-default")
    public ResponseEntity<Boolean> hasDefaultAddress(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.hasDefaultAddress(userId));
    }

    /**
     * Validates and retrieves an address based on user ID, address ID, and address type.
     *
     * @param userId the ID of the user
     * @param addressId the ID of the address
     * @param addressType the type of the address (SHIPPING or BILLING)
     * @return the validated address
     */
    @GetMapping("/validate")
    public ResponseEntity<AddressResponse> validateAndGetAddress(
            @RequestParam UUID userId,
            @RequestParam UUID addressId,
            @RequestParam @Pattern(regexp = "^(SHIPPING|BILLING)$", message = "Address type must be SHIPPING or BILLING") String addressType) {
        return ResponseEntity.ok(addressService.validateAndGetAddress(userId, addressId, addressType));
    }
}