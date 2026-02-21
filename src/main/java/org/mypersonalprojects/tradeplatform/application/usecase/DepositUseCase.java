package org.mypersonalprojects.tradeplatform.application.usecase;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.AssetEnum;
import org.mypersonalprojects.tradeplatform.infra.dto.BalanceDTO;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;

public class DepositUseCase {
    private AccountRepository accountRepository;

    public DepositUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(UUID accountId, BalanceDTO balanceDTO) throws Exception {
        var account = accountRepository.get(accountId);
        var assetEnum = AssetEnum.valueOf(balanceDTO.getAsset());
        account.depositAmount(assetEnum, balanceDTO.getAmount());
        accountRepository.update(account);
    }
}
