package org.mypersonalprojects.tradeplatform.domain;

public class Balance {
    public enum Asset {
        BTC,
        USD
    }

    private Asset asset;
    private Double amount;
    private Double blockedAmount;

    public Balance(String asset, Double amount) {
        this.asset = Asset.valueOf(asset);
        this.amount = validAmount(amount);
        this.blockedAmount = 0.0;
    }

    private Balance(Asset asset, Double amount, Double blockedAmount) {
        this.asset = asset;
        this.amount = amount;
        this.blockedAmount = blockedAmount;
    }

    public static Balance restore(String asset, Double amount, Double blockedAmount) {
        return new Balance(Asset.valueOf(asset), amount, blockedAmount);
    }

    public String getAsset() {
        return asset.toString();
    }

    public Double getAmount() {
        return amount - blockedAmount;
    }

    public Double getBlockedAmount() {
        return blockedAmount;
    }

    public void depositAmount(Double amount) {
        validAmount(amount);
        this.amount = this.amount + amount;
    }

    public void withdrawAmount(Double amount) throws Exception {
        validAmount(amount);
        if (this.amount < amount)
            throw new Exception("Insufficient funds");
        this.amount = this.amount - amount;
    }

    public void blockAmount(Double amount) {
        validAmount(amount);
        if(this.amount < amount)
            throw new RuntimeException("Insufficient funds to block");

        this.blockedAmount = this.blockedAmount + amount;
    }

    public void liquidateAmount(Double amount) {
        if(blockedAmount < amount)
            throw new RuntimeException("Insufficient blocked funds to liquidate");
        this.amount = this.amount - amount;
        this.blockedAmount = this.blockedAmount - amount;
    }

    private Double validAmount(Double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must not be negative");
        return amount;
    }
}
