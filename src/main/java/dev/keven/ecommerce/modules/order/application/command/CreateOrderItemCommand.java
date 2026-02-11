package dev.keven.ecommerce.modules.order.application.command;

import java.math.BigDecimal;

public record CreateOrderItemCommand(
        Long productId,
        int quantity
) {
}
