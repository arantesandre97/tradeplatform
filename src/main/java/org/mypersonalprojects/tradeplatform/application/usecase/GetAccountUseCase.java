package org.mypersonalprojects.tradeplatform.application.usecase;

import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class GetAccountUseCase {
    private final AccountRepository accountRepository;

    public GetAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(String accountId) {
        return accountRepository.getByAccountId(accountId);
    }
}
