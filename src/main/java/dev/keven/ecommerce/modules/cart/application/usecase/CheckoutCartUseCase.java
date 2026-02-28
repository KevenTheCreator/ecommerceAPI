package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.CartContainsUnavailableItemsException;
import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.CheckoutCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CheckoutCartResult;
import dev.keven.ecommerce.modules.order.application.command.ConfirmOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.usecase.ConfirmOrderUseCase;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CheckoutCartUseCase {

    private final OrderGateway orderGateway;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final ProductGateway productGateway;

    public CheckoutCartUseCase(
            OrderGateway orderGateway,
            ConfirmOrderUseCase confirmOrderUseCase,
            ProductGateway productGateway
    ) {
        this.orderGateway = orderGateway;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.productGateway = productGateway;
    }

    @Transactional
    public CheckoutCartResult execute(CheckoutCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseThrow(() -> new OrderNotFoundException("active cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("cannot checkout an empty cart");
        }

        List<Long> unavailableProductIds = cart.getItems().stream()
                .map(item -> item.getProductId())
                .distinct()
                .filter(productId -> productGateway.findById(productId)
                        .map(product -> product.getStatus() != ProductStatus.ACTIVE)
                        .orElse(true))
                .toList();

        if (!unavailableProductIds.isEmpty()) {
            throw new CartContainsUnavailableItemsException(
                    "checkout blocked: cart contains unavailable products " + unavailableProductIds
            );
        }

        var confirmation = confirmOrderUseCase.execute(new ConfirmOrderCommand(command.userId(), cart.getId()));

        return new CheckoutCartResult(
                confirmation.orderId(),
                confirmation.status(),
                confirmation.totalPrice()
        );
    }
}
