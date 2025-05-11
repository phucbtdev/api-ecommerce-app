package com.ecommerce_app.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUpdateRequest {
    @Size(max = 100, message = "Address line 1 cannot exceed 100 characters")
    String addressLine1;

    @Size(max = 100, message = "Address line 2 cannot exceed 100 characters")
    String addressLine2;

    @Size(max = 50, message = "City cannot exceed 50 characters")
    String city;

    @Size(max = 50, message = "State cannot exceed 50 characters")
    String state;

    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    String postalCode;

    @Size(max = 50, message = "Country cannot exceed 50 characters")
    String country;

    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Phone number must be valid")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    String phoneNumber;

    Boolean isDefault;

    @Pattern(regexp = "^(SHIPPING|BILLING|BOTH)$", message = "Address type must be SHIPPING, BILLING, or BOTH")
    String addressType;

    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    String fullName;
}
