package org.mypersonalprojects.tradeplatform.domain;

import java.util.UUID;

import org.mypersonalprojects.tradeplatform.domain.Balance.Asset;

public class Order {
    public enum OrderType {
        BUY,
        SELL
    }

    public enum OrderStatus {
        OPEN,
        FILLED,
        CANCELLED
    }

    private UUID id;
    private String accountId;
    private String marketId;
    private OrderType type;
    private Integer quantity;
    private Integer filledQuantity;
    private Double price;
    private Double averagePrice;
    private OrderStatus status;

    public Order(String accountId, String marketId, String type, Integer quantity, Double price) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.marketId = validMarketId(marketId);
        this.type = OrderType.valueOf(type);
        this.quantity = quantity;
        this.filledQuantity = 0;
        this.price = price;
        this.averagePrice = 0.0;
        this.status = OrderStatus.OPEN;
    }

    private Order(UUID id, String accountId, String marketId, OrderType type, Integer quantity, Integer filledQuantity, 
            Double price, Double averagePrice, OrderStatus status) {
        this.id = id;
        this.accountId = accountId;
        this.marketId = marketId;
        this.type = type;
        this.quantity = quantity;
        this.filledQuantity = filledQuantity;
        this.price = price;
        this.averagePrice = averagePrice;
        this.status = status;
    }

    public static Order restore(String id, String accountId, String marketId, String type, Integer quantity, Integer filledQuantity,
        Double price, Double averagePrice, String status) {
        return new Order(UUID.fromString(id), accountId, marketId, OrderType.valueOf(type), quantity, filledQuantity, price, averagePrice, OrderStatus.valueOf(status));
    }

    private String validMarketId(String marketId) {
        if (marketId.isBlank() || !marketId.matches("^(?!(.+)-\\1$)[^-]+-.+$")) {
            throw new IllegalArgumentException("Invalid market id");
        }

        return marketId;
    }

    public String getId() {
        return id.toString();
    }

    public String getAccountId() {
        return accountId;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getType() {
        return type.toString();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getFilledQuantity() {
        return filledQuantity;
    }

    public Double getPrice() {
        return price;
    }
    
    public Double getAveragePrice() {
        return averagePrice;
    }

    public String getStatus() {
        return status.toString();
    }

    public String getInAsset() {
        return this.type == OrderType.BUY ? 
            Asset.valueOf(marketId.split("-")[1]).toString() : 
            Asset.valueOf(marketId.split("-")[0]).toString();
    }

    public String getOutAsset() {
        return this.type == OrderType.BUY ? 
            Asset.valueOf(marketId.split("-")[0]).toString() : 
            Asset.valueOf(marketId.split("-")[1]).toString();
    };

    public Double getAmount() {
        return this.quantity * this.price;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
