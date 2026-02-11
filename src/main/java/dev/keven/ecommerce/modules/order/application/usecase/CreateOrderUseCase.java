package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.CreateOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.CreateOrderResult;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;

public class CreateOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public CreateOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public CreateOrderResult execute(CreateOrderCommand command) {
        Order order = new Order(command.userId());

        command.items().forEach(item -> {

            Product product = productGateway.findById(item.productId())
                            .orElseThrow(() -> new ProductNotFoundException("product not found"));

            order.addItem(product.getId(), item.quantity(), product.getPrice());
        });

        Order saved = orderGateway.save(order);

        return new CreateOrderResult(
                saved.getId(),
                saved.getTotalPrice(),
                saved.getStatus().name()
        );
    }
}
