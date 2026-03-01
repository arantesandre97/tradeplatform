package org.mypersonalprojects.tradeplatform.application.usecase;

import org.mypersonalprojects.tradeplatform.domain.Order;
import org.mypersonalprojects.tradeplatform.infra.dto.OrderDto;
import org.mypersonalprojects.tradeplatform.infra.repository.AccountRepository;
import org.mypersonalprojects.tradeplatform.infra.repository.OrderRepository;

public class CreateOrderUseCase {
    private AccountRepository accountRepository;
    private OrderRepository orderRepository;

    public CreateOrderUseCase(AccountRepository accountRepository, OrderRepository orderRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
    }

    public void execute(OrderDto orderDto) throws Exception {
        // var account = accountRepository.getByAccountId(orderDto.getAccountId());
        var order = new Order(
            orderDto.getAccountId(), 
            orderDto.getMarketId(), 
            orderDto.getType(), 
            orderDto.getQuantity(), 
            orderDto.getPrice());

        // account.simulateBalanceLiquidation(order.getOutAsset(), order.getAmount());

        // accountRepository.updateAccount(account);
        orderRepository.saveOrder(order);
    }
}
