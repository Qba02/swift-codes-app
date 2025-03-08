package com.jn.swiftcodes.controller;

import com.jn.swiftcodes.dto.BankDetailsInterface;
import com.jn.swiftcodes.dto.BankDto;
import com.jn.swiftcodes.dto.CountrySwiftCodesDto;
import com.jn.swiftcodes.dto.MessageResponseDto;
import com.jn.swiftcodes.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swift-codes")
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/{swift-code}")
    public ResponseEntity<BankDetailsInterface> getBankDetailsBySwiftCode(
            @PathVariable("swift-code") String swiftCode){
        return ResponseEntity.ok(bankService.getBankDetails(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<CountrySwiftCodesDto> getBanksByCountryIso2Code(
            @PathVariable("countryISO2code") String countryIso2Code){
        return ResponseEntity.ok(bankService.getBanksByCountry(countryIso2Code));
    }

    @PostMapping("/")
    public ResponseEntity<MessageResponseDto> createBank(@RequestBody BankDto bankDto){
        return new ResponseEntity<>(bankService.saveBankEntry(bankDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{swift-code}")
    public ResponseEntity<MessageResponseDto> deleteBankBySwiftCode(@PathVariable("swift-code") String swiftCode){
        return ResponseEntity.ok(bankService.deleteBank(swiftCode));
    }

}
