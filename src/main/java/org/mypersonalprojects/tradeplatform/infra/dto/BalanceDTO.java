package org.mypersonalprojects.tradeplatform.infra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class BalanceDTO {
    private String asset;
    private Double amount;

    @JsonCreator
    public BalanceDTO(String asset, Double amount) {
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
