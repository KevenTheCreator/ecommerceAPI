package dev.keven.ecommerce.modules.cart.presentation.mapper;

import dev.keven.ecommerce.modules.cart.application.command.AddItemToCartCommand;
import dev.keven.ecommerce.modules.cart.application.command.CheckoutCartCommand;
import dev.keven.ecommerce.modules.cart.application.command.GetCartCommand;
import dev.keven.ecommerce.modules.cart.application.command.RemoveItemFromCartCommand;
import dev.keven.ecommerce.modules.cart.application.command.UpdateCartItemCommand;
import dev.keven.ecommerce.modules.cart.presentation.dto.request.AddItemToCartRequest;
import dev.keven.ecommerce.modules.cart.presentation.dto.request.UpdateCartItemRequest;
import org.springframework.stereotype.Component;

@Component
public class CartRequestMapper {

    public static GetCartCommand toGetCommand(Long userId) {
        return new GetCartCommand(userId);
    }

    public static AddItemToCartCommand toAddItemCommand(Long userId, AddItemToCartRequest request) {
        return new AddItemToCartCommand(
                userId,
                request.productId(),
                request.quantity()
        );
    }

    public static UpdateCartItemCommand toUpdateItemCommand(Long userId, Long productId, UpdateCartItemRequest request) {
        return new UpdateCartItemCommand(
                userId,
                productId,
                request.quantity()
        );
    }

    public static RemoveItemFromCartCommand toRemoveItemCommand(Long userId, Long productId) {
        return new RemoveItemFromCartCommand(
                userId,
                productId
        );
    }

    public static CheckoutCartCommand toCheckoutCommand(Long userId) {
        return new CheckoutCartCommand(userId);
    }
}
