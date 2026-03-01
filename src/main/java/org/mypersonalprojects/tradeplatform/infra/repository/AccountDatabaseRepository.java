package org.mypersonalprojects.tradeplatform.infra.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.Account;
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

    @Override
    public void saveAccount(Account account) {
        var creationTimestamp = new Timestamp(System.currentTimeMillis());
        jdbcTemplate.update(
                "INSERT INTO tradeplatform.account (account_id, name, email, document, password, creation_date, last_update_date) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)",
                account.getId(), account.getName(), account.getEmail(), account.getDocument(), account.getPassword(), 
                creationTimestamp, creationTimestamp);
    }

    @Override
    public Account getByAccountId(String accountId) {
        List<Balance> balances = jdbcTemplate.query("SELECT asset_id, amount FROM tradeplatform.balance WHERE account_id = ?::uuid",
                (rs, rowNum) -> new Balance(rs.getString("asset_id"), rs.getDouble("amount")),
                accountId);

        try {
            Account account = jdbcTemplate.queryForObject("SELECT * FROM tradeplatform.account WHERE account_id = ?::uuid",
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

    @Override
    public void updateAccount(Account account) {
        for (var balance : account.getBalances()) {
            var updatedBalance = jdbcTemplate.update(
                    "UPDATE tradeplatform.balance SET amount = ?, blocked_amount = ?, last_update_date = ? WHERE account_id = ?::uuid AND asset_id = ?",
                    balance.getAmount(), balance.getBlockedAmount(), new Timestamp(System.currentTimeMillis()), account.getId(), balance.getAsset().toString());
            if (updatedBalance == 0) {
                jdbcTemplate.update("INSERT INTO tradeplatform.balance (account_id, asset_id, amount, blocked_amount, last_update_date) VALUES (?::uuid, ?, ?, ?, ?)",
                        account.getId(), balance.getAsset().toString(), balance.getAmount(), balance.getBlockedAmount(), new Timestamp(System.currentTimeMillis()));
            }
        }
    }
}
