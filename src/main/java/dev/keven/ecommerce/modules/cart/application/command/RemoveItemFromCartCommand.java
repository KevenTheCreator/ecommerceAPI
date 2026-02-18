package dev.keven.ecommerce.modules.cart.application.command;

public record RemoveItemFromCartCommand(
        Long userId,
        Long productId
) {
}
