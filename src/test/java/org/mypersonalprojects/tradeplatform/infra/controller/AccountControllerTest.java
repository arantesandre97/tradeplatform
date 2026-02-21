package org.mypersonalprojects.tradeplatform.infra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class AccountControllerTest {
    private static JdbcTemplate jdbcTemplate;
    private AccountController accountController;

    @BeforeAll
    static void setupDatabase() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        jdbcTemplate.execute("CREATE TABLE account (account_id UUID PRIMARY KEY, name TEXT, email TEXT UNIQUE, document TEXT, password TEXT)");
        jdbcTemplate.execute("CREATE TABLE balance (account_id UUID, asset_id TEXT, amount DOUBLE, PRIMARY KEY (account_id, asset_id), FOREIGN KEY (account_id) REFERENCES account(account_id))");
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM balance");
        jdbcTemplate.execute("DELETE FROM account");
        AccountRepository accountRepository = new AccountDatabaseRepository(jdbcTemplate);
        this.accountController = new AccountController(accountRepository);
    }

    @Test
    @DisplayName("Deve criar uma conta")
    void shouldCreateAnAccount() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123",
            new HashMap<>()
        );

        var response = accountController.signUp(accountDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        var location = response.getHeaders().getLocation().toString();
        var accountId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        var getAccountResponse = accountController.getAccount(accountId);

        assertEquals(HttpStatus.OK, getAccountResponse.getStatusCode());
        assertEquals(accountDto.getEmail(), getAccountResponse.getBody().getEmail());
    }

    @Test
    @DisplayName("Deve retornar erro ao criar conta")
    void shouldReturnErrorWhenCreateAccount() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123",
            new HashMap<>()
        );

        accountController.signUp(accountDto);
        // Try to sign up again with same email (constraint violation)
        var response = accountController.signUp(accountDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve realizar deposito com sucesso")
    void shouldDepositAmount() {
        AccountDTO accountDto = new AccountDTO("John Doe", "john.doe@example.com", "62573679055", "Password@123", new HashMap<>());
        var signUpResponse = accountController.signUp(accountDto);
        var location = signUpResponse.getHeaders().getLocation().toString();
        var accountId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        BalanceDTO request = new BalanceDTO("BTC", 100.0);
        var response = accountController.deposit(accountId, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar erro ao depositar numa conta não existente")
    void shouldReturnErrorWhenDepositAmountWithoutAnExistingAccount() {
        UUID accountId = UUID.randomUUID();
        
        BalanceDTO request = new BalanceDTO("BTC", 100.0);
        var response = accountController.deposit(accountId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve realizar saque com sucesso")
    void shouldWithdrawAmount() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123", 
            new HashMap<>()
        );
        var signUpResponse = accountController.signUp(accountDto);
        var location = signUpResponse.getHeaders().getLocation().toString();
        var accountId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));

        accountController.deposit(accountId, new BalanceDTO("BTC", 200.0));

        BalanceDTO request = new BalanceDTO("BTC", 100.0);
        var response = accountController.withdraw(accountId, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar erro ao sacar sem salfo suficiente")
    void shouldReturnErrorWhenWithdrawAmountWithoutBalance() {
        AccountDTO accountDto = new AccountDTO(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123", 
            new HashMap<>()
        );
        var signUpResponse = accountController.signUp(accountDto);

        var location = signUpResponse.getHeaders().getLocation().toString();
        var accountId = UUID.fromString(location.substring(location.lastIndexOf("/") + 1));
        BalanceDTO request = new BalanceDTO("BTC", 100.0);

        var response = accountController.withdraw(accountId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
