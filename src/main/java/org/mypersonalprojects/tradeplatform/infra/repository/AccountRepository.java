package org.mypersonalprojects.tradeplatform.infra.repository;

import org.mypersonalprojects.tradeplatform.domain.Account;

public interface AccountRepository {
    public void saveAccount(Account account);
    public Account getByAccountId(String accountId);
    public void updateAccount(Account account);
}
