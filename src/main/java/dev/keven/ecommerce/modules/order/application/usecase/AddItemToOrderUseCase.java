package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.AddItemToOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.AddItemResult;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;

import java.util.Optional;

public class AddItemToOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public AddItemToOrderUseCase(OrderGateway orderGateway,  ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public AddItemResult execute(AddItemToOrderCommand command) {
        Order order = orderGateway.findById(command.orderId())
                .orElseThrow(() -> new OrderNotFoundException("orderId not found"));

        Product product = productGateway.findById(command.productId())
                        .orElseThrow(() -> new ProductNotFoundException("product not found"));

        order.addItem(command.productId(), command.quantity(), product.getPrice());

        Order updated = orderGateway.save(order);

        return new AddItemResult(
                updated.getId(),
                updated.getTotalPrice(),
                updated.getTotalQuantity()
        );
    }
}
