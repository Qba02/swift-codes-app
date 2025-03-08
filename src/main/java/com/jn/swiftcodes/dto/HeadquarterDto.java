package com.jn.swiftcodes.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record HeadquarterDto (
        BankDto bank,
        List<BranchDto> branches
) implements BankDetailsInterface { }
