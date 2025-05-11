package com.ecommerce_app.service.implement;

import com.ecommerce_app.dto.request.AddressCreationRequest;
import com.ecommerce_app.dto.request.AddressUpdateRequest;
import com.ecommerce_app.dto.response.AddressResponse;
import com.ecommerce_app.entity.Address;
import com.ecommerce_app.entity.User;
import com.ecommerce_app.exception.BusinessException;
import com.ecommerce_app.exception.ResourceNotFoundException;
import com.ecommerce_app.mapper.AddressMapper;
import com.ecommerce_app.repository.AddressRepository;
import com.ecommerce_app.repository.UserRepository;
import com.ecommerce_app.service.interfaces.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressResponse createAddress(AddressCreationRequest addressCreationRequest) {
        User user = userRepository.findById(addressCreationRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + addressCreationRequest.getUserId()));

        Address address = addressMapper.toEntity(addressCreationRequest, user);

        // If this is the first address or user wants to set it as default
        if (address.getIsDefault()) {
            addressRepository.unsetDefaultAddressesExcept(user.getId(), address.getId()); // No addresses to exclude yet
        } else if (!addressRepository.existsByUserIdAndIsDefaultTrue(user.getId())) {
            // If no default address exists for the user, make this one default
            address.setIsDefault(true);
        }

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(UUID id, AddressUpdateRequest addressUpdateRequest) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        // Store previous default state
        boolean wasDefault = address.getIsDefault();
        boolean wantsToBeDefault = addressUpdateRequest.getIsDefault() != null && addressUpdateRequest.getIsDefault();

        // Update address properties
        addressMapper.updateAddressFromDto(addressUpdateRequest, address);

        // Handle default address logic
        if (wantsToBeDefault && !wasDefault) {
            addressRepository.unsetDefaultAddressesExcept(address.getUser().getId(), address.getId());
            address.setIsDefault(true);
        } else if (wasDefault && addressUpdateRequest.getIsDefault() != null && !addressUpdateRequest.getIsDefault()) {
            // Don't allow unsetting the only default address
            if (addressRepository.findByUserId(address.getUser().getId()).size() > 1) {
                address.setIsDefault(false);
            } else {
                // Keep it as default if it's the only address
                address.setIsDefault(true);
            }
        }

        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByUserId(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findByUserId(userId).stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getShippingAddressesByUserId(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findShippingAddressesByUserId(userId).stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getBillingAddressesByUserId(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findBillingAddressesByUserId(userId).stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getDefaultAddressByUserId(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Address address = addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default address not found for user id: " + userId));

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional
    public AddressResponse setAddressAsDefault(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        // Unset other default addresses for this user
        addressRepository.unsetDefaultAddressesExcept(address.getUser().getId(), address.getId());

        // Set this address as default
        address.setIsDefault(true);
        Address updatedAddress = addressRepository.save(address);

        return addressMapper.toDto(updatedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        // If this is the default address and there are other addresses, throw exception
        if (address.getIsDefault() && addressRepository.findByUserId(address.getUser().getId()).size() > 1) {
            throw new BusinessException("Cannot delete default address. Please set another address as default first.");
        }

        // Check if address is used in any orders
        if (!address.getBillingOrders().isEmpty() || !address.getShippingOrders().isEmpty()) {
            throw new BusinessException("Cannot delete address that is used in orders. Please archive it instead.");
        }

        addressRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasDefaultAddress(UUID userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.existsByUserIdAndIsDefaultTrue(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse validateAndGetAddress(UUID userId, UUID addressId, String addressType) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        // Verify the address belongs to the user
        if (!address.getUser().getId().equals(userId)) {
            throw new BusinessException("Address does not belong to the specified user");
        }

        // Verify the address type is appropriate
        if (addressType.equals("SHIPPING") &&
                !address.getAddressType().equals("SHIPPING") &&
                !address.getAddressType().equals("BOTH")) {
            throw new BusinessException("Address is not designated for shipping");
        }

        if (addressType.equals("BILLING") &&
                !address.getAddressType().equals("BILLING") &&
                !address.getAddressType().equals("BOTH")) {
            throw new BusinessException("Address is not designated for billing");
        }

        return addressMapper.toDto(address);
    }
}