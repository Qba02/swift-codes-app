package com.jn.swiftcodes.service;

import com.jn.swiftcodes.dto.*;
import com.jn.swiftcodes.model.Bank;
import com.jn.swiftcodes.model.Country;
import com.jn.swiftcodes.repository.BankRepository;
import com.jn.swiftcodes.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    private final BankRepository bankRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public BankService(BankRepository bankRepository, CountryRepository countryRepository){
        this.bankRepository = bankRepository;
        this.countryRepository = countryRepository;
    }

    public BankDetailsInterface getBankDetails(String swiftCode){
        Bank bank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new EntityNotFoundException("Bank not found"));

        if(bank.isHeadquarter()){
            return mapToHeadquarter(bank, bankRepository.findByHeadquarters(bank));
        }else{
            return mapToBank(bank);
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
                        .map(this::mapToBranch)
                        .toList())
                .build();
    }

    private HeadquarterDto mapToHeadquarter(Bank bank, List<Bank> branches){
        return HeadquarterDto.builder()
                .bank(mapToBank(bank))
                .branches(branches.stream()
                        .map(this::mapToBranch)
                        .toList())
                .build();
    }

    private BankDto mapToBank(Bank bank){
        return BankDto.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .countryISO2(bank.getCountry().getIso2())
                .countryName(bank.getCountry().getName())
                .isHeadquarter(bank.isHeadquarter())
                .swiftCode(bank.getSwiftCode())
                .build();
    }

    private BranchDto mapToBranch(Bank bank){
        return BranchDto.builder()
                .address(bank.getAddress())
                .bankName(bank.getName())
                .countryISO2(bank.getCountry().getIso2())
                .isHeadquarter(bank.isHeadquarter())
                .swiftCode(bank.getSwiftCode())
                .build();
    }
}
