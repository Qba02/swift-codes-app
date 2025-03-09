package com.jn.swiftcodes.repository;

import com.jn.swiftcodes.model.Bank;
import com.jn.swiftcodes.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BankRepositoryTest {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setup(){
        Bank bank = Bank.builder()
                .swiftCode("ABCDEF12XXX")
                .name("TestBank")
                .build();
        bankRepository.save(bank);
    }

    @Test
    void testFindBySwiftCode() {

        Optional<Bank> foundBank = bankRepository.findBySwiftCode("ABCDEF12XXX");

        assertThat(foundBank).isPresent();
        assertThat(foundBank.get().getName()).isEqualTo("TestBank");
        assertThat(foundBank.get().getSwiftCode()).isEqualTo("ABCDEF12XXX");
    }

    @Test
    void testFindBySwiftCodeStartsWith() {

        Bank foundBank = bankRepository.findBySwiftCodeStartsWith("ABCDEF12");

        assertThat(foundBank).isNotNull();
        assertThat(foundBank.getSwiftCode()).startsWith("ABCDEF12");
    }

    @Test
    void testFindByHeadquarters() {
        Bank headquarters = Bank.builder()
                .swiftCode("HQ123456XXX")
                .name("Headquarters Bank")
                .build();
        bankRepository.save(headquarters);

        Bank branch1 = Bank.builder()
                .swiftCode("HQ123456AAA")
                .name("Branch 1")
                .headquarters(headquarters)
                .build();
        bankRepository.save(branch1);

        Bank branch2 = Bank.builder()
                .swiftCode("HQ123456BBB")
                .name("Branch 2")
                .headquarters(headquarters)
                .build();
        bankRepository.save(branch2);


        List<Bank> branches = bankRepository.findByHeadquarters(headquarters);

        assertThat(branches).hasSize(2);
        assertThat(branches.get(0).getName()).isEqualTo("Branch 1");
        assertThat(branches.get(1).getName()).isEqualTo("Branch 2");
        assertThat(branches.get(0).getSwiftCode()).isEqualTo("HQ123456AAA");
        assertThat(branches.get(1).getSwiftCode()).isEqualTo("HQ123456BBB");
    }

    @Test
    void testFindByCountryId() {
        Country country = new Country();
        country.setIso2("PL");
        country.setName("POLAND");
        country = countryRepository.save(country);

        Bank branch1 = Bank.builder()
                .swiftCode("HQ123456AAA")
                .name("Branch 1")
                .build();
        bankRepository.save(branch1);

        Bank branch2 = Bank.builder()
                .swiftCode("HQ123456BBB")
                .name("Branch 2")
                .build();
        bankRepository.save(branch2);

        List<Bank> banks = bankRepository.findByCountry_Id(country.getId());

        assertThat(banks).hasSize(2);
        assertThat(banks.get(0).getSwiftCode()).isEqualTo("HQ123456AAA");
        assertThat(banks.get(1).getSwiftCode()).isEqualTo("HQ123456BBB");
    }
}
