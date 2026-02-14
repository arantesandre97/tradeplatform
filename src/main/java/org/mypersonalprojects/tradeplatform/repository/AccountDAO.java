package org.mypersonalprojects.tradeplatform.repository;

import java.util.HashMap;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.model.Account;
import org.mypersonalprojects.tradeplatform.model.AssetEnum;
import org.springframework.jdbc.core.JdbcTemplate;

public class AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID save(Account account) {
        jdbcTemplate.update(
                "INSERT INTO ccca.account (account_id, name, email, document, password) VALUES (?, ?, ?, ?, ?)",
                account.getId(), account.getName(), account.getEmail(), account.getDocument(), account.getPassword());

        return account.getId();
    }

    public Account get(UUID accountId) {
        HashMap<AssetEnum, Double> balance = new HashMap<>();
        jdbcTemplate.query("SELECT asset_id, amount FROM ccca.balance WHERE account_id = ?",
                (rs) -> {
                    balance.put(AssetEnum.valueOf(rs.getString("asset_id")), rs.getDouble("amount"));
                },
                accountId);

        Account account = jdbcTemplate.queryForObject("SELECT * FROM ccca.account WHERE account_id = ?",
                (rs, rowNum) -> Account.restore(
                        UUID.fromString(rs.getString("account_id")),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("document"),
                        rs.getString("password"),
                        balance),
                accountId);

        return account;
    }
}
