package com.jn.swiftcodes.service;

import com.jn.swiftcodes.dto.*;
import com.jn.swiftcodes.exception.ErrorMessages;
import com.jn.swiftcodes.exception.SuccessMessages;
import com.jn.swiftcodes.model.Bank;
import com.jn.swiftcodes.model.Country;
import com.jn.swiftcodes.repository.BankRepository;
import com.jn.swiftcodes.repository.CountryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
                .orElseThrow(() -> new EntityNotFoundException("Bank with SWIFT code: " + swiftCode +" not found"));

        if(bank.isHeadquarter()){
            return mapToHeadquarter(bank, bankRepository.findByHeadquarters(bank));
        }else{
            return mapToBank(bank);
        }
    }

    public CountrySwiftCodesDto getBanksByCountry(String countryIso2Code){
        Country country = countryRepository.findCountryByIso2(countryIso2Code)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ErrorMessages.COUNTRY_NOT_FOUND, countryIso2Code))
                );
        return mapToCountrySwiftCodes(country, bankRepository.findByCountry_Id(country.getId()));
    }

    @Transactional
    public MessageResponseDto deleteBank(String swiftCode){
        Bank bank = bankRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ErrorMessages.BANK_DONT_EXISTS, swiftCode)));

        List<Bank> branches = bankRepository.findByHeadquarters(bank);
        branches.forEach(branch -> branch.setHeadquarters(null));
        bankRepository.saveAll(branches);

        bankRepository.delete(bank);
        return new MessageResponseDto(String.format(SuccessMessages.BANK_DELETED, swiftCode));
    }

    @Transactional
    public MessageResponseDto saveBankEntry(BankDto bank) {
        Country country = validateBankEntryData(bank);
        return saveBank(bank, country);
    }


    private Country validateBankEntryData(BankDto bank) {
        Country country = countryRepository.findCountryByIso2(bank.countryISO2())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(ErrorMessages.COUNTRY_NOT_FOUND, bank.countryISO2())
                ));

        bankRepository.findBySwiftCode(bank.swiftCode())
                .ifPresent(existingBank -> {
                    throw new EntityExistsException(
                            String.format(ErrorMessages.BANK_ALREADY_EXISTS, bank.swiftCode())
                    );
                });

        if (!country.getName().equals(bank.countryName())) {
            throw new IllegalArgumentException(ErrorMessages.COUNTRY_NAME_MISMATCH);
        }
        if (bank.swiftCode().endsWith("XXX") != bank.isHeadquarter()) {
            throw new IllegalArgumentException(ErrorMessages.SWIFT_CODE_HEADQUARTER_MISMATCH);
        }
        return country;
    }


    private MessageResponseDto saveBank(BankDto bank, Country country) {

        Bank headquarters = bank.isHeadquarter() ? null
                : bankRepository.findBySwiftCodeStartsWithAndEndsWith(
                        bank.swiftCode().substring(0, 8), "XXX");

        Bank newBank = Bank.builder()
                .country(country)
                .address(bank.address())
                .name(bank.bankName())
                .swiftCode(bank.swiftCode())
                .isHeadquarter(bank.isHeadquarter())
                .headquarters(headquarters)
                .build();

        bankRepository.save(newBank);
        updateBranchesHeadquarterField(newBank);
        return new MessageResponseDto(String.format(SuccessMessages.BANK_SAVED, bank.swiftCode()));
    }

    private void updateBranchesHeadquarterField(Bank newBank) {
        if (newBank.isHeadquarter()) {
            List<Bank> branchesToUpdate = bankRepository.findBySwiftCodeStartsWith(
                            newBank.getSwiftCode().substring(0, 8))
                    .stream()
                    .filter(branch -> branch.getHeadquarters() == null)
                    .filter(branch -> !branch.getSwiftCode().equals(newBank.getSwiftCode()))
                    .toList();

            branchesToUpdate.forEach(branch -> branch.setHeadquarters(newBank));
            bankRepository.saveAll(branchesToUpdate);
        }
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
