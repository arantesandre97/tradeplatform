package org.mypersonalprojects.tradeplatform.infra.dto;

import java.util.HashMap;
import java.util.List;

import org.mypersonalprojects.tradeplatform.domain.Balance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDTO {
    private String name;
    private String email;
    private String document;
    private String password;
    private HashMap<String, Double> balance;

    @JsonCreator
    public AccountDTO(String name, String email, String document, String password, HashMap<String, Double> balance) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.balance = balance;
    }

    //TODO fazer um mapper
    public AccountDTO(String name, String email, String document, List<Balance> balances) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.balance = new HashMap<>();
        if (balances != null) {
            for (var b : balances) {
                this.balance.put(b.getAsset().name(), b.getAmount());
            }
        }
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public HashMap<String, Double> getBalance() {
        return balance;
    }
}
