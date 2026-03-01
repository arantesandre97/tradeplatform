package org.mypersonalprojects.tradeplatform.infra.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mypersonalprojects.tradeplatform.infra.dto.OrderDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountDatabaseRepository;
import org.mypersonalprojects.tradeplatform.infra.repository.OrderDatabaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class OrderControllerTest {
    private static JdbcTemplate jdbcTemplate;
    private OrderController orderController;

    @BeforeAll
    static void setupDatabase() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS tradeplatform");
        jdbcTemplate.execute("CREATE TABLE tradeplatform.account (account_id UUID PRIMARY KEY, name TEXT, email TEXT UNIQUE, document TEXT, password TEXT, creation_date TIMESTAMP, last_update_date TIMESTAMP)");
        jdbcTemplate.execute("CREATE TABLE tradeplatform.balance (account_id UUID, asset_id TEXT, amount DOUBLE, blocked_amount DOUBLE, last_update_date TIMESTAMP, PRIMARY KEY (account_id, asset_id), FOREIGN KEY (account_id) REFERENCES tradeplatform.account(account_id))");
        jdbcTemplate.execute("CREATE TABLE tradeplatform.\"order\" (order_id UUID PRIMARY KEY, account_id UUID, market_id TEXT, order_type TEXT, quantity NUMERIC, filled_quantity NUMERIC, price NUMERIC, order_status TEXT, creation_date TIMESTAMP, last_update_date TIMESTAMP, FOREIGN KEY (account_id) REFERENCES tradeplatform.account(account_id))");
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM tradeplatform.\"order\"");
        jdbcTemplate.execute("DELETE FROM tradeplatform.account");
        var accountRepository = new AccountDatabaseRepository(jdbcTemplate);
        var orderRepository = new OrderDatabaseRepository(jdbcTemplate);
        this.orderController = new OrderController(accountRepository, orderRepository);
    }

    @Test
    @DisplayName("Deve retornar 201 Created ao criar uma ordem com sucesso")
    void shouldReturnCreatedWhenOrderIsSuccessful() {
        var accountId = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO tradeplatform.account (account_id, name, email, document, password) VALUES (?, ?, ?, ?, ?)",
            accountId, "John Doe", "john.doe@test.com", "97456321558", "hashedpass");

        var orderDto = new OrderDto(
            accountId.toString(),
            "BTC-USD",
            "BUY",
            1,
            50000.0
        );

        var response = orderController.createOrder(orderDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        Map<String, Object> orderMap = jdbcTemplate.queryForMap("SELECT * FROM tradeplatform.\"order\" WHERE account_id = ?", accountId);
        assertNotNull(orderMap);
        assertEquals(orderDto.getMarketId(), orderMap.get("market_id"));
        assertEquals(orderDto.getType(), orderMap.get("order_type"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando ocorrer uma exceção")
    void shouldReturnBadRequestWhenExceptionOccurs() {
        var orderDto = new OrderDto(
            UUID.randomUUID().toString(),
            "INVALID",
            "BUY",
            1,
            50000.0
        );

        var response = orderController.createOrder(orderDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
