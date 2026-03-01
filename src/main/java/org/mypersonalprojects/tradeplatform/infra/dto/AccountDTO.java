package org.mypersonalprojects.tradeplatform.infra.dto;

import java.util.HashMap;
import java.util.List;

import org.mypersonalprojects.tradeplatform.domain.Balance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDto {
    private String name;
    private String email;
    private String document;
    private String password;
    private HashMap<String, Double> balances;

    @JsonCreator
    public AccountDto(String name, String email, String document, String password, HashMap<String, Double> balances) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.balances = balances;
    }

    //TODO fazer um mapper
    public AccountDto(String name, String email, String document, List<Balance> balances) {
        this.name = name;
        this.email = email;
        this.document = document;
        this.balances = new HashMap<>();
        if (balances != null) {
            for (var b : balances) {
                this.balances.put(b.getAsset(), b.getAmount());
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
        return balances;
    }
}
