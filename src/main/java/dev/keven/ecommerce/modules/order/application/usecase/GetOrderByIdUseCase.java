package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.GetOrderByIdCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.GetOrderResult;

public class GetOrderByIdUseCase {

    private final OrderGateway orderGateway;

    public GetOrderByIdUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public GetOrderResult execute(GetOrderByIdCommand command) {
        var order = orderGateway.findByIdAndUserId(command.orderId(), command.userId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        return GetOrderResult.from(order);
    }
}
