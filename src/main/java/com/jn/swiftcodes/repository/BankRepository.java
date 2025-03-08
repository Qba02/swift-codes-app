package com.jn.swiftcodes.repository;


import com.jn.swiftcodes.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findBySwiftCode(String swiftCode);
//    Optional<Bank> findBySwiftCodeStartsWith(String swiftCodePrefix);
    List<Bank> findByHeadquarters(Bank headquarters);
    List<Bank> findByCountry_Id(Long countryId);
}
