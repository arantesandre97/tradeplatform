package org.mypersonalprojects.tradeplatform.application.usecase;

import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class WithdrawUseCase {
    private AccountRepository accountRepository;

    public WithdrawUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(String accountId, BalanceDto balanceDto) throws Exception {
        var account = accountRepository.getByAccountId(accountId);
        account.withdrawAmount(balanceDto.getAsset(), balanceDto.getAmount());
        accountRepository.updateAccount(account);
    }
}
