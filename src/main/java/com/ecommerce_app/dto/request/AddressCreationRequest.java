package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {
    @NotNull(message = "User ID cannot be null")
    UUID userId;

    @NotBlank(message = "Address line 1 cannot be blank")
    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    String addressLine1;

    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    String addressLine2;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State cannot exceed 50 characters")
    String state;

    @NotBlank(message = "Postal code cannot be blank")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    String postalCode;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country cannot exceed 50 characters")
    String country;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    String phoneNumber;

    @Builder.Default
    Boolean isDefault = false;

    @NotBlank(message = "Address type cannot be blank")
    @Pattern(regexp = "^(SHIPPING|BILLING|BOTH)$", message = "Address type must be SHIPPING, BILLING, or BOTH")
    String addressType;

    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    String fullName;
}