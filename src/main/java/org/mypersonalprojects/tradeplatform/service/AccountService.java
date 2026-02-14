package org.mypersonalprojects.tradeplatform.service;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.model.Account;
import org.mypersonalprojects.tradeplatform.model.AssetEnum;
import org.mypersonalprojects.tradeplatform.repository.AccountDAO;
import org.mypersonalprojects.tradeplatform.repository.BalanceDAO;

public class AccountService {
    private final AccountDAO accountRespository;
    private final BalanceDAO balanceRepository;

    public AccountService(AccountDAO accountRespository, BalanceDAO balanceRepository) {
        this.accountRespository = accountRespository;
        this.balanceRepository = balanceRepository;
    }

    public UUID createAccount(Account account) {
        return accountRespository.save(account);
    }

    public Account getAccount(UUID accountId) {
        return accountRespository.get(accountId);
    }

    public void depositAmount(UUID accountId, AssetEnum asset, Double amount) throws Exception {
        var account = accountRespository.get(accountId);
        if (account == null) throw new Exception("Account not found");
        account.depositAmount(asset, amount);
        balanceRepository.update(accountId, asset, amount);
    }

    public void withdrawAmount(UUID accountId, AssetEnum asset, Double amount) throws Exception {
        var account = accountRespository.get(accountId);
        if (account == null) throw new Exception("Account not found");
        account.withdrawAmount(asset, amount);
        balanceRepository.update(accountId, asset, amount);
    }
}
