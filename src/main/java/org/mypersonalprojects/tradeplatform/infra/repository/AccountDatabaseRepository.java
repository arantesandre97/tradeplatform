package org.mypersonalprojects.tradeplatform.infra.repository;

import java.util.List;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.Account;
import org.mypersonalprojects.tradeplatform.domain.AssetEnum;
import org.mypersonalprojects.tradeplatform.domain.Balance;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDatabaseRepository implements AccountRepository {
    private final JdbcTemplate jdbcTemplate;

    public AccountDatabaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Account account) {
        jdbcTemplate.update(
                "INSERT INTO account (account_id, name, email, document, password) VALUES (?, ?, ?, ?, ?)",
                account.getId(), account.getName(), account.getEmail(), account.getDocument(), account.getPassword());
    }

    public Account get(UUID accountId) {
        List<Balance> balances = jdbcTemplate.query("SELECT asset_id, amount FROM balance WHERE account_id = ?",
                (rs, rowNum) -> new Balance(AssetEnum.valueOf(rs.getString("asset_id")), rs.getDouble("amount")),
                accountId);

        try {
            Account account = jdbcTemplate.queryForObject("SELECT * FROM account WHERE account_id = ?",
                (rs, rowNum) -> Account.restore(
                        UUID.fromString(rs.getString("account_id")),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("document"),
                        rs.getString("password"),
                        balances),
                accountId);

                return account;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Account not found");
        }
    }

    public void update(Account account) {
        for (var balance : account.getBalances()) {
            var updatedBalance = jdbcTemplate.update(
                    "UPDATE balance SET amount = ? WHERE account_id = ? AND asset_id = ?",
                    balance.getAmount(), account.getId(), balance.getAsset().toString());
            if (updatedBalance == 0) {
                jdbcTemplate.update("INSERT INTO balance (account_id, asset_id, amount) VALUES (?, ?, ?)",
                        account.getId(), balance.getAsset().toString(), balance.getAmount());
            }
        }
    }
}
