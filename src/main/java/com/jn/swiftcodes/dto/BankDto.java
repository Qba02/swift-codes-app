package com.jn.swiftcodes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record BankDto (
        @NotNull(message = "Address cannot be null")
        String address,
        @NotNull(message = "Bank name is required")
        @NotBlank(message = "Bank name is required")
        String bankName,
        @NotNull(message = "Country ISO2 is required")
        @NotBlank(message = "Country ISO2 is required")
        @Size(min = 2, max = 2, message = "Field must be exactly 2 characters long")
        String countryISO2,
        @NotNull(message = "Country name is required")
        @NotBlank(message = "Country name is required")
        String countryName,
        @NotNull(message = "Information about headquarter is required")
        Boolean isHeadquarter,
        @NotNull(message = "SWIFT code is required")
        @NotBlank(message = "SWIFT code is required")
        @Min(value = 8, message = "SWIFT code must be at least 8 characters long")
        String swiftCode
) implements BankDetailsInterface {}
