package org.mypersonalprojects.tradeplatform.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
    private UUID id;
    private String name;
    private String email;
    private String document;
    private String password;
    private List<Balance> balances;

    @JsonCreator
    public Account(@JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("document") String document,
            @JsonProperty("password") String password) {
        this.id = UUID.randomUUID();
        this.name = isValidName(name);
        this.email = isValidEmail(email);
        this.document = isValidDocument(document);
        this.password = isValidPassword(password);
        this.balances = new ArrayList<>();
    }

    private Account(UUID id, String name, String email, String document, String password, List<Balance> balances) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.balances = balances;
    }

    public static Account restore(UUID id, String name, String email, String document, String password,
            List<Balance> balances) {
        return new Account(id, name, email, document, password, balances);
    }

    public void depositAmount(AssetEnum asset, Double amount) {
        balances.stream()
                .filter(b -> b.getAsset().equals(asset))
                .findFirst()
                .ifPresentOrElse(b -> b.depositAmount(amount), () -> {
                    balances.add(new Balance(asset, amount));
                });
    }

    public void withdrawAmount(AssetEnum asset, Double amount) throws Exception {
        balances.stream()
                .filter(b -> b.getAsset().equals(asset))
                .findFirst()
                .ifPresentOrElse(b -> {
                    try {
                        b.withdrawAmount(amount);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }, () -> {
                    throw new RuntimeException("Insufficient funds");
                });
    }

    private String isValidName(String name) {
        if (name.isBlank() || !name.matches("[\\p{L} ]+") || name.split(" ").length < 2) {
            throw new IllegalArgumentException("Invalid name");
        }

        return name;
    }

    private String isValidEmail(String email) {
        if (email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }

        return email;
    }

    private String isValidDocument(String document) {
        if (!ValidateCpf.isValid(document)) {
            throw new IllegalArgumentException("Invalid document");
        }

        return document.replaceAll("\\D", "");
    }

    private String isValidPassword(String password) {
        if (password == null || !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{8,}$")) {
            throw new IllegalArgumentException("Invalid password");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedhash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDocument() {
        return document;
    }

    public String getPassword() {
        return password;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public Balance getBalance(AssetEnum asset) {
        return balances.stream()
                .filter(b -> b.getAsset().equals(asset))
                .findFirst().orElse(new Balance(asset, 0.0));
    }
}
