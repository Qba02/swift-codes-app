package com.jn.swiftcodes.service;

import com.jn.swiftcodes.model.Bank;
import com.jn.swiftcodes.model.Country;
import com.jn.swiftcodes.repository.BankRepository;
import com.jn.swiftcodes.repository.CountryRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CsvParserService {

    private final BankRepository bankRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public CsvParserService(BankRepository bankRepository, CountryRepository countryRepository){
        this.bankRepository = bankRepository;
        this.countryRepository = countryRepository;
    }

    private final List<Bank> banks = new ArrayList<>();
    public void saveDataFromCsvToDatabase(InputStream inputStream) throws IOException, CsvException{
        parseCsvData(inputStream);
        saveBanksToDatabase();
    }

    private void parseCsvData(InputStream inputStream) throws IOException, CsvException{

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                inputStream,
                StandardCharsets.UTF_8)))
        {
            List<String[]> rows = reader.readAll();

            if (!rows.isEmpty()) {
                rows.remove(0);
            }

            for (String[] row : rows) {
                String iso2 = row[0].trim();
                String swiftCode = row[1].trim();
                String bankName = row[3].trim();
                String address = row[4].trim();
                String countryName = row[6].trim();

                banks.add(
                        Bank.builder()
                                .swiftCode(swiftCode)
                                .name(bankName)
                                .address(address)
                                .country(saveCountryToDatabase(countryName, iso2))
                                .isHeadquarter(swiftCode.endsWith("XXX"))
                                .headquarters(null)
                                .build());
            }
        }
    }

    private void saveBanksToDatabase(){

        List<Bank> headquarters = banks.stream()
                .filter(Bank::isHeadquarter)
                .toList();

        List<Bank> branches = banks.stream()
                .filter(bank -> !bank.isHeadquarter())
                .toList();

        bankRepository.saveAll(headquarters);
        Map<String, Bank> headquartersMap = bankRepository.findAll().stream()
                .filter(Bank::isHeadquarter)
                .collect(Collectors.toMap(bank -> bank.getSwiftCode().substring(0, 8), bank -> bank));

        for (Bank branch : branches) {
            Bank hq = headquartersMap.get(branch.getSwiftCode().substring(0, 8));
            branch.setHeadquarters(hq);
        }

        bankRepository.saveAll(branches);
    }

    private Country saveCountryToDatabase(String countryName, String iso2){
        return countryRepository.findCountryByIso2(iso2)
                .orElseGet(() -> countryRepository.save(new Country(null, iso2, countryName)));
    }
}
