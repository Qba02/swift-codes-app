package com.jn.swiftcodes.controller;

import com.jn.swiftcodes.dto.BankDetailsInterface;
import com.jn.swiftcodes.dto.CountrySwiftCodesDto;
import com.jn.swiftcodes.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/swift-codes")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/{swiftCode}")
    public ResponseEntity<BankDetailsInterface> getBankDetailsBySwiftCode(
            @PathVariable String swiftCode){
        return ResponseEntity.ok(bankService.getBankDetails(swiftCode));
    }

    @GetMapping("/country/{countryIso2Code}")
    public ResponseEntity<CountrySwiftCodesDto> getBanksByCountryIso2Code(
            @PathVariable String countryIso2Code){
        return ResponseEntity.ok(bankService.getBanksByCountry(countryIso2Code));
    }

}
