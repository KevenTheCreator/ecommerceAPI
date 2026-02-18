package dev.keven.ecommerce.modules.cart.application.command;

public record AddItemToCartCommand(
        Long userId,
        Long productId,
        int quantity
) {
}
