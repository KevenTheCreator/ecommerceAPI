package dev.keven.ecommerce.modules.order.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.order.application.command.ConfirmOrderCommand;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.application.result.ConfirmOrderResult;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class ConfirmOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public ConfirmOrderUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public ConfirmOrderResult execute(ConfirmOrderCommand command) {
        Order order = orderGateway.findByIdAndUserId(command.orderId(), command.userId())
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        order.getItems().forEach(item -> {
            Product product = productGateway.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("product not found"));

            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new IllegalArgumentException("product is not active");
            }

            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("insufficient stock");
            }

            product.setStock(product.getStock() - item.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            productGateway.update(product);
        });

        order.confirm();

        Order updated = orderGateway.save(order);

        return new ConfirmOrderResult(
                updated.getId(),
                updated.getStatus().name(),
                updated.getItems().size(),
                updated.getTotalPrice().toString()
        );
    }
}
