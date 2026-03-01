package org.mypersonalprojects.tradeplatform.infra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BalanceDto {
    private String asset;
    private Double amount;

    @JsonCreator
    public BalanceDto(String asset, Double amount) {
        this.asset = asset;
        this.amount = amount;
    }

    public String getAsset() {
        return asset;
    }

    public Double getAmount() {
        return amount;
    }
}
