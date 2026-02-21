package org.mypersonalprojects.tradeplatform.application.usecase;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.AssetEnum;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class WithdrawUseCase {
    private AccountRepository accountRepository;

    public WithdrawUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(UUID accountId, BalanceDTO balanceDto) throws Exception {
        var account = accountRepository.get(accountId);
        var assetEnum = AssetEnum.valueOf(balanceDto.getAsset());
        account.withdrawAmount(assetEnum, balanceDto.getAmount());
        accountRepository.update(account);
    }
}
