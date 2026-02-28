package dev.keven.ecommerce.modules.cart.application.usecase;

import dev.keven.ecommerce.modules.cart.application.command.GetCartCommand;
import dev.keven.ecommerce.modules.cart.application.result.CartResult;
import dev.keven.ecommerce.modules.order.application.gateway.OrderGateway;
import dev.keven.ecommerce.modules.order.domain.Order;
import dev.keven.ecommerce.modules.order.domain.OrderStatus;
import dev.keven.ecommerce.modules.product.application.gateway.ProductGateway;
import dev.keven.ecommerce.modules.product.domain.ProductStatus;

import java.util.Optional;

public class GetCartUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public GetCartUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    public CartResult execute(GetCartCommand command) {
        Order cart = orderGateway.findByUserIdAndStatus(command.userId(), OrderStatus.CREATED)
                .orElseGet(() -> orderGateway.save(new Order(command.userId())));

        return CartResult.from(cart, productId -> {
            Optional<ProductStatus> productStatus = productGateway.findById(productId)
                    .map(product -> product.getStatus());

            if (productStatus.isEmpty()) {
                return new CartResult.ItemAvailability(false, "PRODUCT_NOT_FOUND");
            }

            if (productStatus.get() != ProductStatus.ACTIVE) {
                return new CartResult.ItemAvailability(false, "PRODUCT_DEACTIVATED");
            }

            return new CartResult.ItemAvailability(true, null);
        });
    }
}
