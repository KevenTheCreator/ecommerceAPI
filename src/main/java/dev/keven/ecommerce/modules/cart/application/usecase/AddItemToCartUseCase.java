package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.AddItemToCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

public class AddItemToCartUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public AddItemToCartUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public CartResult execute(AddItemToCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseGet(() -> orderGateway.save(new Order(command.userId())));

        Product product = productGateway.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException("product is not active");
        }

        int currentQuantity = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(command.productId()))
                .mapToInt(item -> item.getQuantity())
                .sum();

        if (currentQuantity + command.quantity() > product.getStock()) {
            throw new IllegalArgumentException("insufficient stock");
        }

        cart.addItem(command.productId(), command.quantity(), product.getPrice());
        Order saved = orderGateway.save(cart);

        return CartResult.from(saved);
    }
}
