package dev.keven.ecommerce.modules.order.application.command;

public record GetOrderByIdCommand(
        Long userId,
        Long orderId
) {
}
