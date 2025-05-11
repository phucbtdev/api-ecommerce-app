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

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@RequestBody @Valid AddressCreationRequest addressCreationRequest) {
        return new ResponseEntity<>(addressService.createAddress(addressCreationRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable UUID id,
                                                         @RequestBody @Valid AddressUpdateRequest addressUpdateRequest) {
        return ResponseEntity.ok(addressService.updateAddress(id, addressUpdateRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @GetMapping("/user/{userId}/shipping")
    public ResponseEntity<List<AddressResponse>> getShippingAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getShippingAddressesByUserId(userId));
    }

    @GetMapping("/user/{userId}/billing")
    public ResponseEntity<List<AddressResponse>> getBillingAddressesByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getBillingAddressesByUserId(userId));
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<AddressResponse> getDefaultAddressByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.getDefaultAddressByUserId(userId));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressResponse> setAddressAsDefault(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.setAddressAsDefault(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/has-default")
    public ResponseEntity<Boolean> hasDefaultAddress(@PathVariable UUID userId) {
        return ResponseEntity.ok(addressService.hasDefaultAddress(userId));
    }

    @GetMapping("/validate")
    public ResponseEntity<AddressResponse> validateAndGetAddress(
            @RequestParam UUID userId,
            @RequestParam UUID addressId,
            @RequestParam @Pattern(regexp = "^(SHIPPING|BILLING)$", message = "Address type must be SHIPPING or BILLING") String addressType) {
        return ResponseEntity.ok(addressService.validateAndGetAddress(userId, addressId, addressType));
    }
}
