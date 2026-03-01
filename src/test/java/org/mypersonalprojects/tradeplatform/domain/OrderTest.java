package org.mypersonalprojects.tradeplatform.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.tradeplatform.domain.Order.OrderStatus;
import org.mypersonalprojects.tradeplatform.domain.Order.OrderType;

public class OrderTest {

    @Test
    @DisplayName("Deve criar uma ordem válida")
    void shouldCreateValidOrder() {
        var accountId = UUID.randomUUID().toString();
        var order = new Order(accountId, "BTC-USD", "BUY", 1, 50000.0);

        assertNotNull(order.getId());
        assertEquals(accountId, order.getAccountId());
        assertEquals("BTC-USD", order.getMarketId());
        assertEquals(OrderType.BUY.toString(), order.getType());
        assertEquals(1, order.getQuantity());
        assertEquals(50000.0, order.getPrice());
        assertEquals(OrderStatus.OPEN.toString(), order.getStatus());
    }

    @Test
    @DisplayName("Não deve criar ordem com marketId inválido")
    void shouldThrowExceptionForInvalidMarketId() {
        var accountId = UUID.randomUUID().toString();
        
        var exception = assertThrows(IllegalArgumentException.class, () -> 
            new Order(accountId, "INVALID", "BUY", 1, 50000.0)
        );
        assertEquals("Invalid market id", exception.getMessage());

        assertThrows(IllegalArgumentException.class, () -> 
            new Order(accountId, "BTC-BTC", "BUY", 1, 50000.0)
        );
    }

    @Test
    @DisplayName("Deve calcular o valor total da ordem")
    void shouldCalculateOrderAmount() {
        var order = new Order(UUID.randomUUID().toString(), "BTC-USD", "BUY", 2, 50000.0);
        assertEquals(100000.0, order.getAmount());
    }
}
