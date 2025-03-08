package com.jn.swiftcodes.service;

import com.jn.swiftcodes.dto.BankDetailsInterface;
import com.jn.swiftcodes.dto.BranchDto;
import com.jn.swiftcodes.dto.CountrySwiftCodesDto;
import com.jn.swiftcodes.dto.HeadquarterDto;
import com.jn.swiftcodes.model.Bank;
import com.jn.swiftcodes.model.Country;
import com.jn.swiftcodes.repository.BankRepository;
import com.jn.swiftcodes.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepository bankRepository;
    private final CountryRepository countryRepository;

    public BankDetailsInterface getBankDetails(String swiftCode){
        Bank bank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new EntityNotFoundException("Bank not found"));

        if(bank.isHeadquarter()){
            return mapToHeadquarter(bank, bankRepository.findByHeadquarters(bank));
        }else{
            return mapToBranch(bank);
        }
    }

    public CountrySwiftCodesDto getBanksByCountry(String countryIso2Code){
        Country country = countryRepository.findCountryByIso2(countryIso2Code)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
        return mapToCountrySwiftCodes(country, bankRepository.findByCountry_Id(country.getId()));
    }

    private CountrySwiftCodesDto mapToCountrySwiftCodes(Country country, List<Bank> banks){
        return CountrySwiftCodesDto.builder()
                .countryISO2(country.getIso2())
                .countryName(country.getName())
                .swiftCodes(banks.stream()
                        .map(this::mapToBranchWithoutCountryName)
                        .toList())
                .build();
    }

    private HeadquarterDto mapToHeadquarter(Bank bank, List<Bank> branches){
        return HeadquarterDto.builder()
                .bank(mapToBranchWithoutCountryName(bank))
                .branches(branches.stream()
                        .map(this::mapToBranchWithoutCountryName)
                        .toList())
                .build();
    }

    private BranchDto mapToBranch(Bank bank){
        return BranchDto.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .countryISO2(bank.getCountry().getIso2())
                .countryName(bank.getCountry().getName())
                .isHeadquarter(bank.isHeadquarter())
                .swiftCode(bank.getSwiftCode())
                .build();
    }

    private BranchDto mapToBranchWithoutCountryName(Bank bank){
        return BranchDto.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .countryISO2(bank.getCountry().getIso2())
                .isHeadquarter(bank.isHeadquarter())
                .swiftCode(bank.getSwiftCode())
                .build();
    }
}
