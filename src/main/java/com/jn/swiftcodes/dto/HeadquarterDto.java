package com.jn.swiftcodes.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record HeadquarterDto (
        BranchDto bank,
        List<BranchDto> branches
) implements BankDetailsInterface { }
