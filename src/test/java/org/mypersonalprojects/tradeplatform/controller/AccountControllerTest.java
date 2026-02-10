package org.mypersonalprojects.tradeplatform.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mypersonalprojects.tradeplatform.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class AccountControllerTest {

    @Test
    @DisplayName("Deve criar uma conta")
    void shouldCreateAnAccount() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        AccountController accountController = new AccountController(jdbcTemplate);

        Account account = new Account(
            "John Doe", 
            "john.doe@example.com", 
            "62573679055", 
            "Password@123"
        );

        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(RowMapper.class), eq(account.getId())))
               .thenReturn(account);

        var response = accountController.signUp(account);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        var accountLocation = String.format("/accounts/%s", account.getId());
        Assertions.assertEquals(accountLocation, response.getHeaders().getLocation().toString());

        var getAccountResponse = accountController.getAccount(account.getId());

        Assertions.assertEquals(HttpStatus.OK, getAccountResponse.getStatusCode());
        Assertions.assertEquals(account, getAccountResponse.getBody());
    }
}
