package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.common.exception.OrderNotFoundException;
import dev.keven.ecommerce.common.exception.ProductNotFoundException;
import dev.keven.ecommerce.modules.cart.application.command.UpdateCartItemCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.Product;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;
import org.springframework.transaction.annotation.Transactional;

public class UpdateCartItemUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public UpdateCartItemUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    @Transactional
    public CartResult execute(UpdateCartItemCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseThrow(() -> new OrderNotFoundException("active cart not found"));

        if (command.quantity() < 0) {
            throw new IllegalArgumentException("quantity cannot be negative");
        }

        if (command.quantity() == 0) {
            cart.removeItem(command.productId());
            Order saved = orderGateway.save(cart);
            return CartResult.from(saved);
        }

        Product product = productGateway.findById(command.productId())
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException("product is not active");
        }

        if (command.quantity() > product.getStock()) {
            throw new IllegalArgumentException("insufficient stock");
        }

        cart.updateItemQuantity(command.productId(), command.quantity(), product.getPrice());

        Order saved = orderGateway.save(cart);
        return CartResult.from(saved);
    }
}
