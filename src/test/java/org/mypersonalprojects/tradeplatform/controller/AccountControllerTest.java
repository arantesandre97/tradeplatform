package org.mypersonalprojects.tradeplatform.controller;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mypersonalprojects.tradeplatform.dto.BalanceOperationRequest;
import org.mypersonalprojects.tradeplatform.model.Account;
import org.mypersonalprojects.tradeplatform.model.AssetEnum;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class AccountControllerTest {
    private JdbcTemplate jdbcTemplate;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        this.jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        this.accountController = new AccountController(jdbcTemplate);
    }

    @Test
    @DisplayName("Deve criar uma conta")
    void shouldCreateAnAccount() {
        Account account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        Mockito.when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<RowMapper<Account>>any(), eq(account.getId())))
               .thenReturn(account);

        var response = accountController.signUp(account);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var accountLocation = String.format("/accounts/%s", account.getId());
        Assertions.assertEquals(accountLocation, response.getHeaders().getLocation().toString());

        var getAccountResponse = accountController.getAccount(account.getId());

        Assertions.assertEquals(HttpStatus.OK, getAccountResponse.getStatusCode());
        Assertions.assertEquals(account, getAccountResponse.getBody());
    }

    @Test
    @DisplayName("Deve retornar erro ao criar conta")
    void shouldReturnErrorWhenCreateAccount() {
        Account account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        Mockito.when(jdbcTemplate.update(any(String.class), any(Object[].class)))
               .thenThrow(new RuntimeException("Error creating account"));

        var response = accountController.signUp(account);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve realizar deposito com sucesso")
    void shouldDepositAmount() {
        Account account = new Account("John Doe", "john.doe@example.com", "62573679055", "Password@123");
        
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<RowMapper<Account>>any(), eq(account.getId())))
               .thenReturn(account);

        BalanceOperationRequest request = new BalanceOperationRequest(AssetEnum.BTC, 100.0);
        var response = accountController.deposit(account.getId(), request);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar erro ao depositar")
    void shouldReturnErrorWhenDepositAmount() {
        UUID accountId = UUID.randomUUID();
        
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<RowMapper<Account>>any(), eq(accountId)))
               .thenThrow(new RuntimeException("Account not found"));

        BalanceOperationRequest request = new BalanceOperationRequest(AssetEnum.BTC, 100.0);
        var response = accountController.deposit(accountId, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve realizar saque com sucesso")
    void shouldWithdrawAmount() {
        Account account = new Account("John Doe", "john.doe@example.com", "62573679055", "Password@123");
        account.depositAmount(AssetEnum.BTC, 200.0);
        
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<RowMapper<Account>>any(), eq(account.getId())))
               .thenReturn(account);

        BalanceOperationRequest request = new BalanceOperationRequest(AssetEnum.BTC, 100.0);
        var response = accountController.withdraw(account.getId(), request);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar erro ao sacar")
    void shouldReturnErrorWhenWithdrawAmount() {
        Account account = new Account("John Doe", "john.doe@example.com", "62573679055", "Password@123");
        
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), ArgumentMatchers.<RowMapper<Account>>any(), eq(account.getId())))
               .thenReturn(account);

        BalanceOperationRequest request = new BalanceOperationRequest(AssetEnum.BTC, 100.0);
        var response = accountController.withdraw(account.getId(), request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
