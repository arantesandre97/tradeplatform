package org.mypersonalprojects.tradeplatform.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DocumentTest {
    @Test
    void shouldCreateDocumentWithCorrectCpf() {
        var cpf = "97456321558";
        Assertions.assertDoesNotThrow(() -> new Document(cpf));
    }

    @Test
    void shouldNotCreateDocumentWithIncorrectCpf() {
        var cpf = "11111111111";
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Document(cpf));
    }

    @Test
    void shouldNotCreateDocumentWithEmptyCpf() {
        var cpf = "";
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Document(cpf));
    }
}
