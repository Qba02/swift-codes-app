package com.jn.swiftcodes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BankDto (
        String address,
        @NotNull(message = "Bank name is required")
        String bankName,
        @NotNull(message = "Country ISO2 is required")
        String countryISO2,
        @NotNull(message = "Country name is required")
        String countryName,
        @NotNull(message = "Information about headquarter is required")
        boolean isHeadquarter,
        @NotNull(message = "Swift code is required")
        String swiftCode
) implements BankDetailsInterface {}
