package org.mypersonalprojects.tradeplatform.application.usecase;

import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.infra.dto.AccountDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class CreateAccountUseCase {
    private final AccountRepository accountRespository;

    public CreateAccountUseCase(AccountRepository accountRespository) {
        this.accountRespository = accountRespository;
    }

    public String execute(AccountDto accountDto) {
        var account = new Account(accountDto.getName(), accountDto.getEmail(), accountDto.getDocument(),
                accountDto.getPassword());
        accountRespository.saveAccount(account);
        return account.getId();
    }
}
