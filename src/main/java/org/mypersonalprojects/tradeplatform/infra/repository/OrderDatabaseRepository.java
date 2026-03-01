package org.mypersonalprojects.tradeplatform.infra.repository;

import java.sql.Timestamp;
import java.util.List;
import org.mypersonalprojects.tradeplatform.domain.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderDatabaseRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderDatabaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveOrder(Order order) {
        var creationTimestamp = new Timestamp(System.currentTimeMillis());     
        jdbcTemplate.update(
                "INSERT INTO tradeplatform.\"order\" (order_id, account_id, market_id, order_type, quantity, filled_quantity, price, order_status, creation_date, last_update_date) VALUES (?::uuid, ?::uuid, ?, ?, ?, ?, ?, ?, ?, ?)",
                order.getId(), order.getAccountId(), order.getMarketId(), order.getType(), order.getQuantity(), order.getFilledQuantity(), order.getPrice(), order.getStatus(),
                creationTimestamp, creationTimestamp);
    }

    @Override
    public Order getOrderById(String orderId) {
        try {
            Order order = jdbcTemplate.queryForObject("SELECT * FROM tradeplatform.\"order\" WHERE order_id = ?::uuid",
                (rs, rowNum) -> Order.restore(
                        rs.getString("order_id"),
                        rs.getString("account_id"),
                        rs.getString("market_id"),
                        rs.getString("order_type"),
                        rs.getInt("quantity"),
                        rs.getInt("filled_quantity"),
                        rs.getDouble("price"),
                        0.0, // average_price is not in database
                        rs.getString("order_status")),
                orderId);

                return order;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Order not found");
        }
    }

    @Override
    public void updateOrder(Order order) {
        jdbcTemplate.update("UPDATE tradeplatform.\"order\" SET order_status = ? WHERE order_id = ?::uuid", order.getStatus(), order.getId());      
    }

    @Override
    public List<Order> getOrdersByMatchedOrder(Order order) {
        var orderTypeFilter = "BUY".equalsIgnoreCase(order.getType()) ? "SELL" : "BUY";
        var openOrders = jdbcTemplate.queryForStream("SELECT * FROM tradeplatform.\"order\" WHERE market_id = ? AND order_type = ? AND order_status = ?", 
            (rs, rowNum) -> Order.restore(
                        rs.getString("order_id"),
                        rs.getString("account_id"),
                        rs.getString("market_id"),
                        rs.getString("order_type"),
                        rs.getInt("quantity"),
                        rs.getInt("filled_quantity"),
                        rs.getDouble("price"),
                        0.0, // average_price is not in database
                        rs.getString("order_status")),
                order.getMarketId(), orderTypeFilter, "OPEN");

        return openOrders.toList();
    }
}
