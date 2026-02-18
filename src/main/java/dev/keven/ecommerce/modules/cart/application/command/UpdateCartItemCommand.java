package dev.keven.ecommerce.modules.cart.application.command;

public record UpdateCartItemCommand(
        Long userId,
        Long productId,
        int quantity
) {
}
