package com.jn.swiftcodes;

import com.jn.swiftcodes.controller.BankController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SmokeTest {

    @Autowired
    private BankController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}