package org.mypersonalprojects.tradeplatform.controller;

import java.net.URI;
import java.util.UUID;

import org.mypersonalprojects.tradeplatform.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final JdbcTemplate jdbcTemplate;

    public AccountController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/signup")
    public ResponseEntity<URI> signUp(@RequestBody Account account) {
        jdbcTemplate.update(
            "INSERT INTO ccca.account (account_id, name, email, document, password) VALUES (?, ?, ?, ?, ?)",
            account.getId(), account.getName(), account.getEmail(), account.getDocument(), account.getPassword()
        );
        var accountLocation = String.format("/accounts/%s", account.getId());
        return ResponseEntity.created(URI.create(accountLocation)).build();
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID accountId) {
        try {
            Account account = jdbcTemplate.queryForObject("SELECT * FROM ccca.account WHERE account_id = ?", 
                (rs, rowNum) -> Account.restore(
                    UUID.fromString(rs.getString("account_id")),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("document"),
                    rs.getString("password")
                ), accountId);
            return ResponseEntity.ok(account);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
