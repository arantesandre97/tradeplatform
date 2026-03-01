package org.mypersonalprojects.tradeplatform.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Account {
    private UUID id;
    private Name name;
    private Email email;
    private Document document;
    private Password password;
    private List<Balance> balances;

    public Account(String name, String email, String document, String password) {
        this.id = UUID.randomUUID();
        this.name = new Name(name);
        this.email = new Email(email);
        this.document = new Document(document);
        this.password = Password.create(password);
        this.balances = new ArrayList<>();
    }

    private Account(UUID id, Name name, Email email, Document document, Password password, List<Balance> balances) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.document = document;
        this.password = password;
        this.balances = balances;
    }

    public static Account restore(UUID id, String name, String email, String document, String password,
            List<Balance> balances) {
        return new Account(id, new Name(name), new Email(email), new Document(document), Password.restore(password), balances);
    }

    public void depositAmount(String asset, Double amount) {
        var balance = getBalance(asset);

        if (balance.isPresent()) {
            balance.get().depositAmount(amount);
        } else {
            balances.add(new Balance(asset, amount));
        }
    }

    public void withdrawAmount(String asset, Double amount) throws Exception {
        var balance = getBalance(asset);

        if (balance.isPresent()) {
            balance.get().withdrawAmount(amount);
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    public void simulateBalanceLiquidation(String outAsset, Double amount) throws Exception {
        var outBalance = getBalance(outAsset);

        if (outBalance.isPresent()) {
            outBalance.get().blockAmount(amount);
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    public void liquidate(String inAsset, String outAsset, Double amount) throws Exception {
        var outBalance = getBalance(outAsset);

        if(!outBalance.isPresent())
            throw new Exception("Insufficient funds");

        outBalance.get().liquidateAmount(amount);
        var inBalance = getBalance(inAsset);

        if(inBalance.isPresent()){
            inBalance.get().depositAmount(amount);
        } else {
            balances.add(new Balance(inAsset, amount));
        }
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getDocument() {
        return document.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public Optional<Balance> getBalance(String asset) {
        return balances.stream()
                .filter(b -> b.getAsset().equals(asset))
                .findFirst();
    }
}
