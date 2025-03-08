package com.jn.swiftcodes.repository;

import com.jn.swiftcodes.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findCountryByIso2(String iso2Code);
}
