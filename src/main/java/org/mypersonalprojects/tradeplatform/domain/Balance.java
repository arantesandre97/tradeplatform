package org.mypersonalprojects.tradeplatform.domain;

public class Balance {
    private AssetEnum asset;
    private Double amount;

    public Balance(AssetEnum asset, Double amount) {
        this.asset = asset;
        this.amount = validAmount(amount);
    }

    public AssetEnum getAsset() {
        return asset;
    }

    public Double getAmount() {
        return amount;
    }

    public void depositAmount(Double amount) {
        validAmount(amount);
        this.amount = this.amount + amount;
    }

    public void withdrawAmount(Double amount) throws Exception {
        validAmount(amount);
        if(this.amount < amount) throw new Exception("Insufficient funds");
        this.amount = this.amount - amount;
    }

    private Double validAmount(Double amount) {
        if(amount < 0) throw new IllegalArgumentException("Amount must not be negative");
        return amount;
    }
}
