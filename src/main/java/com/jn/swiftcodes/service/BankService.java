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

    public MessageResponseDto saveBankEntry(BankDto bank) {
        Country country = countryRepository.findCountryByIso2(bank.countryISO2())
                .orElseThrow(() -> new EntityNotFoundException("Country ISO2 not found - cannot add bank entry"));

        if (bankRepository.findBySwiftCode(bank.swiftCode()).isPresent()) {
            throw new IllegalArgumentException("Bank with SWIFT code: " + bank.swiftCode() + " already exists.");
        }
        if (!country.getName().equals(bank.countryName())) {
            throw new IllegalArgumentException("Country with provided ISO2 found, but country name does not match.");
        }
        return saveBank(bank, country);
    }

    public MessageResponseDto deleteBank(String swiftCode){
        Bank bank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Bank with SWIFT code: " + swiftCode + " does not exists."));
        bankRepository.delete(bank);
        return new MessageResponseDto("Successfully removed bank with SWIFT code: " + swiftCode);
    }

    private MessageResponseDto saveBank(BankDto bank, Country country) {

        Bank headquarters = bank.isHeadquarter() ? null
                : bankRepository.findBySwiftCodeStartsWith(bank.swiftCode().substring(0, 8));
//                .orElseThrow(() -> new EntityNotFoundException("No headquarters found for branch"));

        Bank newBank = Bank.builder()
                .country(country)
                .address(bank.address())
                .name(bank.bankName())
                .swiftCode(bank.swiftCode())
                .isHeadquarter(bank.isHeadquarter())
                .headquarters(headquarters)
                .build();

        bankRepository.save(newBank);
        return new MessageResponseDto("Successfully saved bank with SWIFT code: " + bank.swiftCode());
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
