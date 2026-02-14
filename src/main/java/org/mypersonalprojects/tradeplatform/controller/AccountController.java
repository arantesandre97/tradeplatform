package org.mypersonalprojects.tradeplatform.controller;

import java.net.URI;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.dto.BalanceOperationRequest;
import org.mypersonalprojects.tradeplatform.model.Account;
import org.mypersonalprojects.tradeplatform.repository.AccountDAO;
import org.mypersonalprojects.tradeplatform.repository.BalanceDAO;
import org.mypersonalprojects.tradeplatform.service.AccountService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {    
    private final AccountService accountService;

    public AccountController(JdbcTemplate jdbcTemplate) {
        this.accountService = new AccountService(
            new AccountDAO(jdbcTemplate), 
            new BalanceDAO(jdbcTemplate));
    }

    @PostMapping("/signup")
    public ResponseEntity<URI> signUp(@RequestBody Account account) {
        try {
            accountService.createAccount(account);
            var accountLocation = String.format("/accounts/%s", account.getId());
            return ResponseEntity.created(URI.create(accountLocation)).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID accountId) {
        try {
            var account = accountService.getAccount(accountId);
            return ResponseEntity.ok(account);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable UUID accountId, @RequestBody BalanceOperationRequest balanceOperationRequest) {
        try {
            accountService.depositAmount(accountId, balanceOperationRequest.getAsset(), balanceOperationRequest.getAmount());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable UUID accountId, @RequestBody BalanceOperationRequest balanceOperationRequest) {
        try {
            accountService.withdrawAmount(accountId, balanceOperationRequest.getAsset(), balanceOperationRequest.getAmount());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
