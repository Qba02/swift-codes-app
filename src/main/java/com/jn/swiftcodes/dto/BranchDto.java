package com.jn.swiftcodes.dto;

import lombok.Builder;

@Builder
public record BranchDto (
        String address,
        String bankName,
        String countryISO2,
        boolean isHeadquarter,
        String swiftCode
){}
