package org.mypersonalprojects.tradeplatform.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidateCpfTest {
    @Test
    void shouldValidateACorrectCpf() {
        var cpf = "97456321558";
        var isValid = ValidateCpf.isValid(cpf);
        Assertions.assertTrue(isValid);
    }

    @Test
    void shouldNotValidateAnIncorrectCpf() {
        var cpf = "11111111111";
        var isValid = ValidateCpf.isValid(cpf);
        Assertions.assertFalse(isValid);
    }

    @Test
    void shouldNotValidateAnEmptyCpf() {
        var cpf = "";
        var isValid = ValidateCpf.isValid(cpf);
        Assertions.assertFalse(isValid);
    }
}
