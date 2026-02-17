package dev.keven.ecommerce.modules.order.application.command;

public record ConfirmOrderCommand(
        Long userId,
        Long orderId
) {
}
