package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.modules.cart.application.command.GetCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;

public class GetCartUseCase {

    private final OrderGateway orderGateway;

    public GetCartUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    public CartResult execute(GetCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseGet(() -> orderGateway.save(new Order(command.userId())));

        return CartResult.from(cart);
    }
}
