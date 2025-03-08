package com.jn.swiftcodes.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CountrySwiftCodesDto(
        String countryISO2,
        String countryName,
        List<BranchDto> swiftCodes
) { }
