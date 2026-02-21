package org.mypersonalprojects.tradeplatform.infra.repository;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.Account;

public interface AccountRepository {
    public void save(Account account);
    public Account get(UUID accountId);
    public void update(Account account);
}
