package org.mypersonalprojects.tradeplatform.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );
    }

    @Test
    @DisplayName("Deve criar uma conta")
    void shouldCreateAnAccount() {
        Account newAccount = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        Assertions.assertEquals("John Doe", newAccount.getName());
        Assertions.assertEquals("john.doe@example.com", newAccount.getEmail());
        Assertions.assertEquals("62573679055", newAccount.getDocument());
        Assertions.assertNotEquals("Password@123", newAccount.getPassword());
        Assertions.assertNotNull(newAccount.getPassword());
    }

    @Test
    @DisplayName("Deve criar uma conta com documento contendo caracteres especiais")
    void shouldCreateAnAccountWithADocumentWithSpecialCaracters() {
        Account newAccount = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "625.736.790-55", 
            "Password@123"
        );

        Assertions.assertEquals("John Doe", newAccount.getName());
        Assertions.assertEquals("john.doe@example.com", newAccount.getEmail());
        Assertions.assertEquals("62573679055", newAccount.getDocument());
        Assertions.assertNotEquals("Password@123", newAccount.getPassword());
        Assertions.assertNotNull(newAccount.getPassword());
    }

    @Test
    @DisplayName("Não deve criar conta sem sobrenome")
    void shouldNotCreateAnAccountWithoutLastName() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com nome contendo caracteres especiais ou números")
    void shouldNotCreateAnAccountWithNameContainingSpecialCaractersAndNumbers() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John @123", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com email inválido")
    void shouldNotCreateAnAccountWithInvalidEmail() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe.example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com documento inválido")
    void shouldNotCreateAnAccountWithInvalidDocument() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "123456789", 
            "Password@123"
            );
        });

        Assertions.assertEquals("Invalid document", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com senha inválida")
    void shouldNotCreateAnAccountWithInvalidPassword() {
        var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "password123"
            );
        });

        Assertions.assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    @DisplayName("Deve depositar um valor positivo")
    void shouldDepositAmount() {
        account.depositAmount(AssetEnum.BTC, 100.0);
        Assertions.assertEquals(100.0, account.getBalance(AssetEnum.BTC));
    }

    @Test
    @DisplayName("Deve acumular saldo com múltiplos depósitos")
    void shouldAccumulateBalance() {
        account.depositAmount(AssetEnum.BTC, 100.0);
        account.depositAmount(AssetEnum.BTC, 50.0);
        Assertions.assertEquals(150.0, account.getBalance(AssetEnum.BTC));
    }

    @Test
    @DisplayName("Deve lançar exceção ao depositar valor negativo")
    void shouldThrowExceptionWhenDepositNegativeAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.depositAmount(AssetEnum.BTC, -10.0);
        });
    }

    @Test
    @DisplayName("Deve sacar um valor quando houver saldo suficiente")
    void shouldWithdrawAmount() throws Exception {
        account.depositAmount(AssetEnum.USDT, 200.0);
        account.withdrawAmount(AssetEnum.USDT, 50.0);
        Assertions.assertEquals(150.0, account.getBalance(AssetEnum.USDT));
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar valor maior que o saldo")
    void shouldThrowExceptionWhenWithdrawInsufficientFunds() {
        account.depositAmount(AssetEnum.USDT, 50.0);
        Assertions.assertThrows(Exception.class, () -> {
            account.withdrawAmount(AssetEnum.USDT, 100.0);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar ativo sem saldo")
    void shouldThrowExceptionWhenWithdrawNoAsset() {
        Assertions.assertThrows(Exception.class, () -> {
            account.withdrawAmount(AssetEnum.BTC, 10.0);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar valor negativo")
    void shouldThrowExceptionWhenWithdrawNegativeAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            account.withdrawAmount(AssetEnum.USDT, -10.0);
        });
    }
}
