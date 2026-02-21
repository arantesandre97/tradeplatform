package org.mypersonalprojects.tradeplatform.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        assertEquals("John Doe", newAccount.getName());
        assertEquals("john.doe@example.com", newAccount.getEmail());
        assertEquals("62573679055", newAccount.getDocument());
        assertNotEquals("Password@123", newAccount.getPassword());
        assertNotNull(newAccount.getPassword());
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

        assertEquals("John Doe", newAccount.getName());
        assertEquals("john.doe@example.com", newAccount.getEmail());
        assertEquals("62573679055", newAccount.getDocument());
        assertNotEquals("Password@123", newAccount.getPassword());
        assertNotNull(newAccount.getPassword());
    }

    @Test
    @DisplayName("Não deve criar conta sem sobrenome")
    void shouldNotCreateAnAccountWithoutLastName() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com nome contendo caracteres especiais ou números")
    void shouldNotCreateAnAccountWithNameContainingSpecialCaractersAndNumbers() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John @123", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        assertEquals("Invalid name", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com email inválido")
    void shouldNotCreateAnAccountWithInvalidEmail() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe.example.com", 
            "62573679055", 
            "Password@123"
            );
        });

        assertEquals("Invalid email", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com documento inválido")
    void shouldNotCreateAnAccountWithInvalidDocument() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "123456789", 
            "Password@123"
            );
        });

        assertEquals("Invalid document", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve criar conta com senha inválida")
    void shouldNotCreateAnAccountWithInvalidPassword() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "password123"
            );
        });

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    @DisplayName("Deve depositar um valor positivo")
    void shouldDepositAmount() {
        account.depositAmount(AssetEnum.BTC, 100.0);

        assertEquals(100.0, account.getBalance(AssetEnum.BTC).getAmount());
    }

    @Test
    @DisplayName("Deve acumular saldo com múltiplos depósitos")
    void shouldAccumulateBalance() {
        account.depositAmount(AssetEnum.BTC, 100.0);
        account.depositAmount(AssetEnum.BTC, 50.0);
        
        assertEquals(150.0, account.getBalance(AssetEnum.BTC).getAmount());
    }

    @Test
    @DisplayName("Deve lançar exceção ao depositar valor negativo")
    void shouldThrowExceptionWhenDepositNegativeAmount() {
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            account.depositAmount(AssetEnum.BTC, -10.0);
        });

        assertEquals(exception.getMessage(), "Amount must not be negative");
    }

    @Test
    @DisplayName("Deve sacar um valor quando houver saldo suficiente")
    void shouldWithdrawAmount() throws Exception {
        account.depositAmount(AssetEnum.USD, 200.0);
        account.withdrawAmount(AssetEnum.USD, 50.0);

        assertEquals(150.0, account.getBalance(AssetEnum.USD).getAmount());
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar valor maior que o saldo")
    void shouldThrowExceptionWhenWithdrawInsufficientFunds() {
        account.depositAmount(AssetEnum.USD, 50.0);
        var exception = assertThrows(Exception.class, () -> {
            account.withdrawAmount(AssetEnum.USD, 100.0);
        });

        assertEquals(exception.getMessage(), "Insufficient funds");
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar de um ativo que não existe na conta")
    void shouldThrowExceptionWhenWithdrawNoAsset() {
        var exception = assertThrows(Exception.class, () -> {
            account.withdrawAmount(AssetEnum.BTC, 10.0);
        });

        assertEquals(exception.getMessage(), "Insufficient funds");
    }

    @Test
    @DisplayName("Deve lançar exceção ao sacar valor negativo")
    void shouldThrowExceptionWhenWithdrawNegativeAmount() {
        account.depositAmount(AssetEnum.USD, 100.0);
        var exception = assertThrows(RuntimeException.class, () -> {
            account.withdrawAmount(AssetEnum.USD, -10.0);
        });

        assertEquals(exception.getMessage(), "Amount must not be negative");
    }
}
