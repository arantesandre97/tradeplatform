package org.mypersonalprojects.tradeplatform.infra.controller;

import java.net.URI;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.application.usecase.CreateAccountUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.DepositUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.GetAccountUseCase;
import org.mypersonalprojects.tradeplatform.application.usecase.WithdrawUseCase;
import org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//TODO implement the error handling
@RestController
public class AccountController {
    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountUseCase getAccountUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;

    public AccountController(AccountRepository accountRepository) {
        this.createAccountUseCase = new CreateAccountUseCase(accountRepository);
        this.getAccountUseCase = new GetAccountUseCase(accountRepository);
        this.depositUseCase = new DepositUseCase(accountRepository);
        this.withdrawUseCase = new WithdrawUseCase(accountRepository);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO accountDto) {
        try {
            var accountId = createAccountUseCase.execute(accountDto);
            var accountLocation = String.format("/accounts/%s", accountId);
            return ResponseEntity.created(URI.create(accountLocation)).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable UUID accountId) {
        try {
            var account = getAccountUseCase.execute(accountId);
            var accountDto = new org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO(
                account.getName(),
                account.getEmail(),
                account.getDocument(),
                account.getBalances()
            );
            return ResponseEntity.ok(accountDto);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable UUID accountId, @RequestBody BalanceDTO balanceDto) {
        try {
            depositUseCase.execute(accountId, balanceDto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable UUID accountId, @RequestBody BalanceDTO balanceDto) {
        try {
            withdrawUseCase.execute(accountId, balanceDto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
