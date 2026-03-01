package org.mypersonalprojects.tradeplatform.infra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class OrderDto {
    private String accountId;
    private String marketId;
    private String type;
    private Integer quantity;
    private Double price;

    @JsonCreator
    public OrderDto(String accountId, String marketId, String type, Integer quantity, Double price) {
        this.accountId = accountId;
        this.marketId = marketId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getType() {
        return type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }  
}
