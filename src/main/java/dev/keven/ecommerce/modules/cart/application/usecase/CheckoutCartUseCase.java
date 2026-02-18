package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.CheckoutCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CheckoutCartResult;
import dev.keven.ecommerce.modules.order.application.command.ConfirmOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.usecase.ConfirmOrderUseCase;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import org.springframework.transaction.annotation.Transactional;

public class CheckoutCartUseCase {

    private final OrderGateway orderGateway;
    private final ConfirmOrderUseCase confirmOrderUseCase;

    public CheckoutCartUseCase(OrderGateway orderGateway, ConfirmOrderUseCase confirmOrderUseCase) {
        this.orderGateway = orderGateway;
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    @Transactional
    public CheckoutCartResult execute(CheckoutCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseThrow(() -> new OrderNotFoundException("active cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("cannot checkout an empty cart");
        }

        var confirmation = confirmOrderUseCase.execute(new ConfirmOrderCommand(command.userId(), cart.getId()));

        return new CheckoutCartResult(
                confirmation.orderId(),
                confirmation.status(),
                confirmation.totalPrice()
        );
    }
}
