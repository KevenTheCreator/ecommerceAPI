package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.RemoveItemFromCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import org.springframework.transaction.annotation.Transactional;

public class RemoveItemFromCartUseCase {

    private final OrderGateway orderGateway;

    public RemoveItemFromCartUseCase(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Transactional
    public CartResult execute(RemoveItemFromCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseThrow(() -> new OrderNotFoundException("active cart not found"));

        cart.removeItem(command.productId());

        Order saved = orderGateway.save(cart);
        return CartResult.from(saved);
    }
}
