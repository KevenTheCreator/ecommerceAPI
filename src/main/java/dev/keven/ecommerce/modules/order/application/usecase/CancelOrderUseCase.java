package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.CancelOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.CancelOrderResult;
import dev.keven.ecommerce.modules.order.domain.Order;

public class CancelOrderUseCase {

    private final OrderGateway orderGateway;

    public CancelOrderUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public CancelOrderResult execute(CancelOrderCommand command) {
        Order order = orderGateway.findByIdAndUserId(command.orderId(), command.userId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.cancel();

        Order updated = orderGateway.save(order);

        return new CancelOrderResult(
                updated.getId(),
                updated.getStatus().name()
        );
    }
}
