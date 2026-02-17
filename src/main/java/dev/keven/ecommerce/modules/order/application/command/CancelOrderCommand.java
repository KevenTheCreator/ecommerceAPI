package dev.keven.ecommerce.modules.order.application.command;

public record CancelOrderCommand(
        Long userId,
        Long orderId
) {
}
