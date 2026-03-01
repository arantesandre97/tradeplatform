package org.mypersonalprojects.tradeplatform.infra.repository;

import java.util.List;
import org.mypersonalprojects.tradeplatform.domain.Order;

public interface OrderRepository {
    public void saveOrder(Order order);
    public Order getOrderById(String orderId);
    public List<Order> getOrdersByMatchedOrder(Order order);
    public void updateOrder(Order order);
}
