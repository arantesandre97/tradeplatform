package org.mypersonalprojects.tradeplatform.domain;

import java.util.List;

public class Book {
    private List<Order> orders;

    public Book(List<Order> orders) {
        this.orders = orders;
    }

    public void proccess(Order order) {
 
    }

    public List<Order> getOrders() {
        return orders;
    }
}
