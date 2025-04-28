package com.desafiofullstask.votacao.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ValidationUtilTest {

    @Autowired
    private ValidationUtil validationUtil;

    @Test
    void validateCPF() {
        boolean isValid = validationUtil.validateCPF("235.080.746-09");
        assert (isValid);
    }

    @Test
    void validateCPFSaveWithoutZeroInFront() {
        boolean isValid = validationUtil.validateCPF("47.273.822-45");
        assert (isValid);
    }

    @Test
    void validateCPFInvalid() {
        boolean isValid = validationUtil.validateCPF("265.080.746-00");
        assert (!isValid);
    }

    @Test
    void validateCPFWithNoPoints() {
        boolean isValid = validationUtil.validateCPF("23508074609");
        assert (isValid);
    }
}