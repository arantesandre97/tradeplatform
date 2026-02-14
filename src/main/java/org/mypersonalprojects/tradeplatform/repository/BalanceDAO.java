package org.mypersonalprojects.tradeplatform.repository;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.model.AssetEnum;
import org.springframework.jdbc.core.JdbcTemplate;

public class BalanceDAO {
    private final JdbcTemplate jdbcTemplate;

    public BalanceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void update(UUID accountId, AssetEnum asset, Double amount) {
        var updatedBalance = jdbcTemplate.update("UPDATE ccca.balance SET amount = amount WHERE account_id = ? AND asset_id = ?",
                amount, accountId, asset);
        if (updatedBalance == 0) {
            jdbcTemplate.update("INSERT INTO ccca.balance (account_id, asset_id, amount) VALUES (?, ?, ?)",
                    accountId, asset, amount);
        }
    }
}
