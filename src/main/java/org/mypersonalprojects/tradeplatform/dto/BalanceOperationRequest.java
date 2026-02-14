package org.mypersonalprojects.tradeplatform.dto;

import org.mypersonalprojects.tradeplatform.model.AssetEnum;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BalanceOperationRequest {
    private AssetEnum asset;
    private Double amount;

    @JsonCreator
    public BalanceOperationRequest(AssetEnum asset, Double amount) {
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be a positive value");
        this.asset = asset;
        this.amount = amount;
    }

    public AssetEnum getAsset() {
        return asset;
    }

    public Double getAmount() {
        return amount;
    }
}
