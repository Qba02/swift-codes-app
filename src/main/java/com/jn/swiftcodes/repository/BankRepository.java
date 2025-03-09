package com.jn.swiftcodes.repository;


import com.jn.swiftcodes.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findBySwiftCode(String swiftCode);
    @Query("SELECT b FROM Bank b WHERE b.swiftCode LIKE CONCAT(:prefix, '%', :suffix)")
    Bank findBySwiftCodeStartsWithAndEndsWith(@Param("prefix") String prefix, @Param("suffix") String suffix);
    List<Bank> findByHeadquarters(Bank headquarters);
    List<Bank> findByCountry_Id(Long countryId);
}
