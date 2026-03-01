package org.mypersonalprojects.tradeplatform.application.usecase;

import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class DepositUseCase {
    private AccountRepository accountRepository;

    public DepositUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(String accountId, BalanceDto balanceDTO) throws Exception {
        var account = accountRepository.getByAccountId(accountId);
        account.depositAmount(balanceDTO.getAsset(), balanceDTO.getAmount());
        accountRepository.updateAccount(account);
    }
}
