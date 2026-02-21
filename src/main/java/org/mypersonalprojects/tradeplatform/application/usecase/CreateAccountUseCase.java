package org.mypersonalprojects.tradeplatform.application.usecase;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.infra.dto.AccountDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class CreateAccountUseCase {
    private final AccountRepository accountRespository;

    public CreateAccountUseCase(AccountRepository accountRespository) {
        this.accountRespository = accountRespository;
    }

    public UUID execute(AccountDTO accountDto) {
        var account = new Account(accountDto.getName(), accountDto.getEmail(), accountDto.getDocument(),
                accountDto.getPassword());
        accountRespository.save(account);
        return account.getId();
    }
}
