package com.jn.swiftcodes.dto;

import lombok.Builder;

@Builder
public record BranchDto (
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode
) implements BankDetailsInterface {}
