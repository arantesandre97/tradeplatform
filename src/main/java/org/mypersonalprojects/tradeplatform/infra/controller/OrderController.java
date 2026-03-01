package org.mypersonalprojects.tradeplatform.infra.controller;

import org.mypersonalprojects.tradeplatform.application.usecase.CreateOrderUseCase;
import org.mypersonalprojects.tradeplatform.infra.dto.OrderDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;
import org.mypersonalprojects.tradeplatform.infra.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private CreateOrderUseCase createOrderUseCase;

    public OrderController(AccountRepository accountRepository,OrderRepository orderRepository) {
        this.createOrderUseCase = new CreateOrderUseCase(accountRepository, orderRepository);
    }

    
    @PostMapping("/orders")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto) {
        try {
            createOrderUseCase.execute(orderDto);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
