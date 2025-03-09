package com.jn.swiftcodes.exception;

public class ErrorMessages {
    public static final String COUNTRY_NOT_FOUND = "Country with ISO2 code: %s not found";
    public static final String BANK_ALREADY_EXISTS = "Bank with SWIFT code: %s already exists.";
    public static final String BANK_DONT_EXISTS = "Bank with SWIFT code: %s does not exists.";
    public static final String COUNTRY_NAME_MISMATCH = "Country with provided ISO2 found, " +
            "but country name does not match.";
    public static final String SWIFT_CODE_HEADQUARTER_MISMATCH =
            "Invalid SWIFT code and headquarters status combination: Banks with SWIFT code ending with 'XXX' " +
                    "must have isHeadquarter = true, while branches must have isHeadquarter = false.";
}

